package com.rakuten.ecld.wms.wombatoutbound.architecture.common;

import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.ResponseStyle;
import org.springframework.stereotype.Component;

@Component
public class YNQuestionInfo implements StepHandler {

    public void execute(CliHandler cliHandler) {
        cliHandler.response("y/n？", ResponseStyle.INFO);
    }
}
