package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.rakuten.ecld.wms.wombatoutbound.constant.CommandConstant.ITEM_CODE_REGEX;

@Component
@Slf4j
public class ItemCodeEstimate extends AbstractBaseStepHandler<PtgState> {
    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> item code estimate");

        if (!cliHandler.getInput().equals(cliHandler.getState().getItem().getItemCode())){
            cliHandler.fail(messageSourceUtil.getMessage("outbound.common.item_problem"));
        }
    }

    @Override
    public boolean isInputValid(CliHandler<PtgState> cliHandler) {
        return !isInputEmpty(cliHandler) && matchPattern(cliHandler,ITEM_CODE_REGEX);
    }
}
