package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.rakuten.ecld.wms.wombatoutbound.constant.CommandConstant.INTEGER_REGEX;

@Component
@Slf4j
@RequiredArgsConstructor
public class SDNumberEstimate extends AbstractBaseStepHandler<PtgState> {

    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> short/damage number estimate");

        if (Integer.parseInt(cliHandler.getInput()) > cliHandler.getState().getNumberExcludeBadItem()) {
            cliHandler.fail(messageSourceUtil.getMessage("outbound.common.number.above"));
        }
    }

    @Override
    public boolean isInputValid(CliHandler<PtgState> cliHandler) {
        return !isInputEmpty(cliHandler) && matchPattern(cliHandler, INTEGER_REGEX);
    }
}
