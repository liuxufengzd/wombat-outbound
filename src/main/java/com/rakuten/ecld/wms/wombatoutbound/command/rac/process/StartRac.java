package com.rakuten.ecld.wms.wombatoutbound.command.rac.process;

import com.rakuten.ecld.wms.wombatclib.api.CliHandler;
import com.rakuten.ecld.wms.wombatclib.api.StepHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.rac.model.RacState;
import org.springframework.stereotype.Component;

@Component
public class StartRac implements StepHandler<RacState> {

    @Override public void execute(CliHandler<RacState> cliHandler) {
        cliHandler.response("hello rac");
    }
}
