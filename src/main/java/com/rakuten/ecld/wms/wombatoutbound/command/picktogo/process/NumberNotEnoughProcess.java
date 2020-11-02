package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import com.rakuten.ecld.wms.wombatoutbound.service.command.common.BadItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NumberNotEnoughProcess extends AbstractBaseStepHandler<PtgState> {
    private final BadItemService badItemService;

    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> number not enough estimate");

        int number = cliHandler.getState().getNumberExcludeBadItem() - badItemService.badItemNumber(cliHandler.getState().getItem());
        cliHandler.response(messageSourceUtil.getMessage("outbound.command.ptg.not_enough_number",new String[]{String.valueOf(number)}));
    }
}
