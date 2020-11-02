package com.rakuten.ecld.wms.wombatoutbound.command.itemfix.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.itemfix.model.IfState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartItemFixProcess extends AbstractBaseStepHandler<IfState> {
    @Override
    public void process(CliHandler<IfState> cliHandler) {
        log.info("step --> start item fix process");

        cliHandler.response(messageSourceUtil.getMessage("outbound.command.ifix.start_ifix"));
    }
}
