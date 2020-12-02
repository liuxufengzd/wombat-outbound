package com.rakuten.ecld.wms.wombatoutbound.command.picktogo;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.BaseCommandHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.common.CommandHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.common.StepHandlerFactory;
import com.rakuten.ecld.wms.wombatoutbound.architecture.core.Model;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.Action;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.InputType;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import static com.rakuten.ecld.wms.wombatoutbound.constant.CommandConstant.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class PickToGo extends BaseCommandHandler<PtgState> implements CommandHandler {
    private final StepHandlerFactory stepHandlerFactory;
    private Model model;

    @Override
    public String getCommand() {
        return "picktogo";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"ptg"};
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public Object generateState() {
        return new PtgState();
    }

    @Override
    public void define() {
        log.info("Inside the pick to go command process");
        Model model = new Model(stepHandlerFactory);
        this.model = model
                .mainFlow()
                    .step("start-ptg", true).run(StartPickToGoProcess.class)
                    .step("delivery-question").size(DELIVERY_CODE_MAX).commandCanBeChanged().run(DeliveryQuestion.class)
                    .step("delivery-estimate", true).run(DeliveryEstimate.class)
                    .step("start-register").run(RegisterStartProcess.class)
                    .step("register-question").size(DELIVERY_CODE_MAX).allowShortcut(Action.CHGDELI).run(DeliveryQuestion.class)
                    .step("register-estimate", true).run(RegisterDeliveryEstimate.class)
                    .step("box-label-question").size(BOX_LABEL_MAX).allowShortcut(Action.CHGDELI).run(BoxLabelQuestion.class)
                    .step("box-label-estimate", true).run(BoxLabelEstimate.class)
                    .step("register-confirm").run(RegisterConfirmProcess.class)
                    .step("register-uncompleted", c->!hasBatchRegistered(c)).continueAt("register-question")
                    .step("complete-register").run(RegisterCompleteProcess.class)
                    .step("start-pick").run(FindPickItemProcess.class)
                    .step("item-not-found",this::nextItemNotFound).continueAt("QA-process")
                    .step("identity-confirm", this::isVeteran).continueAt("item-code-question")
                    .rootStep("shelf-question").size(SHELF_CODE_MAX).allowShortcut(Action.BREAK, Action.SHORT, Action.DAMAGE).run(ShelfQuestion.class)
                    .step("shelf-estimate", true).run(ShelfEstimate.class)
                    .rootStep("item-code-question").size(ITEM_CODE_MAX).allowShortcut(Action.BREAK, Action.SHORT, Action.DAMAGE).run(ItemCodeQuestion.class)
                    .step("item-code-confirm", true).run(ItemCodeEstimate.class)
                    .rootStep("number-question").size(NUMBER_MAX).inputType(InputType.DECIMAL).allowShortcut(Action.BREAK, Action.SHORT, Action.DAMAGE).run(NumberQuestion.class)
                    .step("number-estimate", true).run(NumberEstimate.class)
                    .YNStep("number-question")
                    .rootStep("pick-box-question").size(BOX_LABEL_MAX).allowShortcut(Action.BREAK, Action.SHORT, Action.DAMAGE).run(PickBoxQuestion.class)
                    .step("pick-box-estimate",true).run(PickBoxEstimate.class)
                    .step("batch-estimate").run(BatchEstimate.class)
                    .step("batch-not-picked", c->!hasPickFinished(c)).continueAt("start-pick")
                    .step("QA-process").run(QAProcess.class)
                    .step("continue-question").run(ContinueQuestion.class)
                    .YNStep(null,"delivery-question")
                    .step("end-ptg").run(EndPickToGoProcess.class)

                .flow("batch-registered").after(this::hasBatchRegistered, "delivery-estimate")
                    .step("pick-finished", this::hasPickFinished).continueAt("end-ptg")
                    .step("continue-process").runAndContinueAt(ContinueProcess.class, "start-pick")
                .flow("change-delivery-code", true)
                    .after(this::hasUserEnteredCdl, "register-question", "box-label-question")
                    .runAndContinueAt(RecoverBoxAreaProcess.class, "delivery-question")
                .flow("break-flow",true)
                    .after(this::hasUserEnteredBreak,"shelf-question","item-code-question","number-question","pick-box-question")
                    .step("break-question").run(PtgBreakQuestion.class)
                    .YNStep("rootStep")
                .flow("short-flow",true)
                    .after(this::hasUserEnteredShort,"shelf-question","item-code-question","number-question","pick-box-question")
                    .step("short-question").run(ShortProcess.class)
                    .YNStep("rootStep")
                    .step("short-login-complete").runAndContinueAt(ShortCompleteProcess.class,"batch-estimate")
                .flow("damage-flow",true)
                    .after(this::hasUserEnteredDamage,"shelf-question","item-code-question","number-question","pick-box-question")
                    .step("damage-question").run(DamageProcess.class)
                    .YNStep("rootStep")
                    .step("damage-login-complete").runAndContinueAt(DamageCompleteProcess.class,"batch-estimate")
                .flow("sd-number-more-than-one").after(this::numberMoreThanOne,"short-question","damage-question")
                    .step("sd-number-question").size(NUMBER_MAX).inputType(InputType.DECIMAL).run(NumberQuestion.class)
                    .step("sd-login-cancel",this::hasUserEnteredCancel,true).continueAtRoot()
                    .step("sd-number-valid").run(SDNumberEstimate.class)
                    .step("short-number-not-enough",this::numberNotEnough).callerIs("short-question").runAndContinueAt(ShortCompleteProcess.class,"number-confirm-flow")
                    .step("damage-number-not-enough",this::numberNotEnough).callerIs("damage-question").runAndContinueAt(DamageCompleteProcess.class,"number-confirm-flow")
                    .step("short-number-enough").callerIs("short-question").continueAt("short-login-complete")
                    .step("damage-number-enough").callerIs("damage-question").continueAt("damage-login-complete")
                .flow("number-confirm-flow").run(NumberNotEnoughProcess.class)
                    .step("number-cancel-entered",this::hasUserEnteredCancel,true).continueAt("shelf-question")
                    .step("number-short-entered",this::hasUserEnteredShort).continueAt("short-question")
                    .step("number-damage-entered",this::hasUserEnteredDamage).continueAt("damage-question")

                .build();
    }

    private boolean hasBatchRegistered(CliHandler<PtgState> cliHandler) {
        return cliHandler.getState().isBatchRegistered();
    }

    private boolean hasPickFinished(CliHandler<PtgState> cliHandler) {
        return cliHandler.getState().isPickFinished();
    }

    private boolean nextItemNotFound(CliHandler<PtgState> cliHandler) {
        return !cliHandler.getState().isNextItemFound();
    }

    private boolean numberMoreThanOne(CliHandler<PtgState> cliHandler){
        return cliHandler.getState().getNumberExcludeBadItem() > 1;
    }

    private boolean numberNotEnough(CliHandler<PtgState> cliHandler){
        return Integer.parseInt(cliHandler.getInput()) < cliHandler.getState().getNumberExcludeBadItem();
    }
}
