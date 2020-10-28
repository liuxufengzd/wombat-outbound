package com.rakuten.ecld.wms.wombatoutbound.architecture.common;

import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import org.springframework.stereotype.Component;

@Component
public class YNEstimate extends AbstractBaseStepHandler {
    @Override
    public void process(CliHandler cliHandler) {
        String input = cliHandler.getInput();
        if (!"y".equals(input) && !"n".equals(input))
            cliHandler.fail("y/nで入力してください");
    }

    @Override
    public boolean isInputValid(CliHandler cliHandler) {
        return !isInputEmpty(cliHandler);
    }
}
