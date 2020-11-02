package com.rakuten.ecld.wms.wombatoutbound.command.itemfix.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.itemfix.model.IfState;
import com.rakuten.ecld.wms.wombatoutbound.service.command.common.BadItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.rakuten.ecld.wms.wombatoutbound.constant.CommandConstant.ITEM_CODE_REGEX;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemFixItemCodeEstimate extends AbstractBaseStepHandler<IfState> {
    private final BadItemService badItemService;

    @Override
    public void process(CliHandler<IfState> cliHandler) {
        log.info("step --> item code estimate");

        badItemService.fixBadItem(cliHandler.getInput());
        cliHandler.response(messageSourceUtil.getMessage("outbound.command.ifix.item_complete",cliHandler.getInput()));
        cliHandler.response(messageSourceUtil.getMessage("outbound.command.ifix.continue_ifix"));
    }

    @Override
    public boolean isInputValid(CliHandler<IfState> cliHandler) {
        return !isInputEmpty(cliHandler) && matchPattern(cliHandler,ITEM_CODE_REGEX);
    }
}
