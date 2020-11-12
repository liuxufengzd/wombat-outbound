package com.rakuten.ecld.wms.wombatoutbound.command.itemfix;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.BaseCommandHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.common.CommandHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.common.StepHandlerFactory;
import com.rakuten.ecld.wms.wombatoutbound.architecture.core.Model;
import com.rakuten.ecld.wms.wombatoutbound.command.itemfix.model.IfState;
import com.rakuten.ecld.wms.wombatoutbound.command.itemfix.process.EndItemFixProcess;
import com.rakuten.ecld.wms.wombatoutbound.command.itemfix.process.ItemFixItemCodeEstimate;
import com.rakuten.ecld.wms.wombatoutbound.command.itemfix.process.StartItemFixProcess;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process.ItemCodeQuestion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemFix extends BaseCommandHandler<IfState> implements CommandHandler {
    private final StepHandlerFactory stepHandlerFactory;
    private Model model;

    @Override
    public String getCommand() {
        return "itemfix";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"ifix"};
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public Object generateState() {
        return new IfState();
    }

    @Override
    public void define() {
        log.info("Inside the item fix command process");
        Model model = new Model(stepHandlerFactory);
        this.model = model
            .mainFlow()
                .step("start-item-fix",true).run(StartItemFixProcess.class)
                .step("item-code-question").run(ItemCodeQuestion.class)
                .step("item-code-estimate",true).run(ItemFixItemCodeEstimate.class)
                .YNStep(null,"item-code-question")
                .step("end-item-fix").run(EndItemFixProcess.class)
            .build();
    }


}
