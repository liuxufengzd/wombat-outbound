package com.rakuten.ecld.wms.wombatoutbound.command.rac;

import com.rakuten.ecld.wms.wombatclib.api.CliData;
import com.rakuten.ecld.wms.wombatclib.api.CliModel;
import com.rakuten.ecld.wms.wombatclib.api.CliModelBuilder;
import com.rakuten.ecld.wms.wombatclib.api.CommandHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.rac.model.RacState;
import com.rakuten.ecld.wms.wombatoutbound.command.rac.process.StartRac;
import org.springframework.stereotype.Component;

@Component
public class Rac implements CommandHandler {

    @Override public String getCommand() {
        return "racode";
    }

    @Override public String[] getAlias() {

        return new String[]{"rac"};
    }

    @Override public CliModel define(CliModelBuilder cliModelBuilder) {
        return cliModelBuilder.state(new RacState())
            .basicFlow()
                .step("startRac").user().system(StartRac.class)
            .flow("start-flow").after("startRac").condition(this::test)
                .step("restart").continuesAt("startRac")
            .build();
    }

    private boolean test(CliData<RacState> cliData) {
        return false;
    }
}
