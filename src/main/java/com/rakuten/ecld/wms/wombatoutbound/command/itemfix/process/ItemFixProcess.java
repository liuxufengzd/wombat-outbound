package com.rakuten.ecld.wms.wombatoutbound.command.itemfix.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.itemfix.model.IfState;
import com.rakuten.ecld.wms.wombatoutbound.service.common.BadItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemFixProcess extends AbstractBaseStepHandler<IfState> {
    private final BadItemService badItemService;

    @Override
    public void process(CliHandler<IfState> cliHandler) {
        log.info("step --> item fix process");

        badItemService.fixBadItem(cliHandler.getInput());
    }
}
