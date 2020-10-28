package com.rakuten.ecld.wms.wombatoutbound.architecture.common;

import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import org.springframework.stereotype.Component;

@Component
public class EmptyHandler implements StepHandler {

    public void execute(CliHandler cliHandler) {}
}
