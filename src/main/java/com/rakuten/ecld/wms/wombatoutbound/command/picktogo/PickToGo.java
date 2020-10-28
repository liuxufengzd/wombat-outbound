package com.rakuten.ecld.wms.wombatoutbound.command.picktogo;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.BaseCommandHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.common.CommandHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.common.HandlerFactory;
import com.rakuten.ecld.wms.wombatoutbound.architecture.core.Model;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.Action;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.InputType;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PickToGo extends BaseCommandHandler<PtgState> implements CommandHandler {
    private final HandlerFactory handlerFactory;
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
        Model model = new Model(handlerFactory);
        this.model = model
                .mainFlow()
                    .step("start-ptg", true).run(StartPtg.class)
                    .step("delivery-question").size(25).commandCanBeChanged().run(DeliveryQuestion.class)
                    .step("delivery-estimate", true).run(DeliveryEstimate.class)
                    .step("start-register").run(RegisterStartProcess.class)
                    .step("register-question").size(25).allowShortcut(Action.CHGDELI).run(DeliveryQuestion.class)
                    .step("register-estimate", true).run(RegisterDeliveryEstimate.class)
                    .step("box-label-question").size(14).allowShortcut(Action.CHGDELI).run(BoxLabelQuestion.class)
                    .step("box-label-estimate", true).run(BoxLabelEstimate.class)
                    .step("register-confirm").run(RegisterConfirmProcess.class)
                    .step("register-uncompleted", this::hasBatchNotRegistered).continueAt("register-question")
                    .step("start-pick").run(FindPickItemProcess.class)
                    .step("identity-confirm", this::isVeteran).continueAt("item-code-question")
                    .rootStep("shelf-question").size(13).allowShortcut(Action.BREAK,Action.SHORT,Action.DAMAGE).run(ShelfQuestion.class)
                    .step("shelf-estimate", true).run(ShelfEstimate.class)
                    .rootStep("item-code-question").size(20).allowShortcut(Action.BREAK,Action.SHORT,Action.DAMAGE).run(ItemCodeQuestion.class)
                    .step("item-code-confirm", true).run(ItemCodeEstimate.class)
                    .rootStep("number-question").size(10).inputType(InputType.DECIMAL).allowShortcut(Action.BREAK,Action.SHORT,Action.DAMAGE).run(NumberQuestion.class)
                    .step("number-estimate", true).run(NumberEstimate.class)
                    .YNStep("number-question")
                    .rootStep("pick-box-question").size(14).allowShortcut(Action.BREAK,Action.SHORT,Action.DAMAGE).run(PickBoxQuestion.class)
                    .step("pick-box-estimate", true).run(PickBoxEstimate.class)
                    .step("batch-estimate").run(BatchEstimate.class)
                    .step("batch-not-picked", this::hasPickNotFinished).continueAt("start-pick")
                    .step("QA-estimate").run(QAEstimate.class)
                    .step("QA-process",this::qaExist).run(QAProcess.class)
                    .YNStep(null,"start-ptg")

                .flow("batch-registered").after(this::hasBatchRegistered, "delivery-estimate")
                    .step("pick-finished", this::hasPickFinished).continueAt("delivery-question")
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
                    .step("short-login-complete").runAndContinueAt(ShortComplete.class,"batch-estimate")
                .flow("damage-flow",true)
                    .after(this::hasUserEnteredDamage,"shelf-question","item-code-question","number-question","pick-box-question")
                    .step("damage-question").run(DamageProcess.class)
                    .YNStep("rootStep")
                    .step("damage-login-complete").runAndContinueAt(DamageComplete.class,"batch-estimate")
                .flow("sd-number-more-than-one").after(this::numberMoreThanOne,"short-question","damage-question")
                    .step("sd-number-question").size(10).inputType(InputType.DECIMAL).run(NumberQuestion.class)
                    .step("sd-login-cancel",this::hasUserEnteredCancel,true).continueAtRoot()
                    .step("sd-number-valid").run(SDNumberEstimate.class)
                    .step("sd-number-not-enough",this::numberNotEnough).continueAt("number-confirm-flow")
                    .step("short-number-enough").callerIs("short-question").continueAt("short-login-complete")
                    .step("damage-number-enough").callerIs("damage-question").continueAt("damage-login-complete")
                .flow("number-confirm-flow").run(NumberNotEnoughEstimate.class)
                    .step("number-cancel-entered",this::hasUserEnteredCancel,true).continueAtRoot()
                    .step("number-short-entered",this::hasUserEnteredShort).continueAt("short-question")
                    .step("number-damage-entered",this::hasUserEnteredDamage).continueAt("damage-question")

                .build();
    }

    private boolean hasBatchRegistered(CliHandler<PtgState> cliHandler) {
        return true;
    }

    private boolean hasPickFinished(CliHandler<PtgState> cliHandler) {
        return true;
    }

    private boolean hasPickNotFinished(CliHandler<PtgState> cliHandler) {
        return true;
    }

    private boolean hasBatchNotRegistered(CliHandler<PtgState> cliHandler) {
        return true;
    }

    private boolean numberMoreThanOne(CliHandler<PtgState> cliHandler){
        return true;
    }

    private boolean numberNotEnough(CliHandler<PtgState> cliHandler){
        return true;
    }

    private boolean qaExist(CliHandler<PtgState> cliHandler){
        return true;
    }
}
