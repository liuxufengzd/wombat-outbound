package com.rakuten.ecld.wms.wombatoutbound.architecture.common;

import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import org.springframework.stereotype.Component;

@Component
public class ReturnProcess implements StepHandler {

    @Override
    public void execute(CliHandler cliHandler) {
        cliHandler.setReturn();
    }
}
