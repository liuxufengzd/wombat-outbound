package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.rakuten.ecld.wms.wombatoutbound.constant.CommandConstant.SHELF_CODE_REGEX;

@Component
@Slf4j
public class ShelfEstimate extends AbstractBaseStepHandler<PtgState> {
    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> shelf estimate");

        String expectedShelfCode = cliHandler.getState().getItem().getShelfCode();
        if (!cliHandler.getInput().equals(expectedShelfCode)){
            cliHandler.fail(messageSourceUtil.getMessage("outbound.common.shelf.error"));
        }
    }

    @Override
    public boolean isInputValid(CliHandler<PtgState> cliHandler) {
        return !isInputEmpty(cliHandler) && matchPattern(cliHandler,SHELF_CODE_REGEX);
    }
}
