package com.rakuten.ecld.wms.wombatoutbound.architecture.common;

import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import org.springframework.stereotype.Component;

@Component
public class EndConfirmQuestion implements StepHandler {

    public void execute(CliHandler cliHandler) {
        cliHandler.response("作業終了しますか？");
        cliHandler.response("y/n？");
    }
}
