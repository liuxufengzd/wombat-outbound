package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.rakuten.ecld.wms.wombatoutbound.constant.CommandConstant.POSITIVE_INTEGER_REGEX;

@Component
@Slf4j
public class NumberEstimate extends AbstractBaseStepHandler<PtgState> {

    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> number estimate");

        int input = Integer.parseInt(cliHandler.getInput());
        if (cliHandler.getState().getNumberExcludeBadItem() < input) {
            cliHandler.fail(messageSourceUtil.getMessage("outbound.common.number.above"));
            return;
        }
        cliHandler.getState().setPickNumber(input);
    }

    @Override
    public boolean isInputValid(CliHandler<PtgState> cliHandler) {
        return !isInputEmpty(cliHandler) && matchPattern(cliHandler, POSITIVE_INTEGER_REGEX);
    }
}
