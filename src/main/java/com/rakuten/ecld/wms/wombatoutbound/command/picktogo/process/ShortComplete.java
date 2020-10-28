package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShortComplete extends AbstractBaseStepHandler {

    @Override public void process(CliHandler cliHandler) {
        log.info("in short complete process");

        cliHandler.response(messageSourceUtil.getMessage("outbound.common.short.login_complete"));
    }
}
