package com.rakuten.ecld.wms.wombatoutbound.command.itemfix.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.itemfix.model.IfState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EndItemFixProcess extends AbstractBaseStepHandler<IfState> {

    @Override
    public void process(CliHandler<IfState> cliHandler) {
        cliHandler.response(messageSourceUtil.getMessage("outbound.command.ifix.end_ifix"));
    }
}
