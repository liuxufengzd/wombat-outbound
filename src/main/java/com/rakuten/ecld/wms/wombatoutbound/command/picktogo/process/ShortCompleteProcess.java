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
public class ShortCompleteProcess extends AbstractBaseStepHandler<PtgState> {
    private final BadItemService badItemService;

    @Override public void process(CliHandler<PtgState> cliHandler) {
        log.info("in short complete process");

        String input = cliHandler.getInput();
        int numberExcludeBadItem = cliHandler.getState().getNumberExcludeBadItem();
        if ("y".equals(input)){
            badItemService.badItemLogin(cliHandler.getState().getItem(), "short", 1);
            cliHandler.getState().setNumberExcludeBadItem(numberExcludeBadItem - 1);
            cliHandler.response(messageSourceUtil.getMessage("outbound.common.short.login_complete","1"));
        }
        else {
            badItemService.badItemLogin(cliHandler.getState().getItem(), "short", Integer.parseInt(input));
            cliHandler.getState().setNumberExcludeBadItem(numberExcludeBadItem - Integer.parseInt(input));
            cliHandler.response(messageSourceUtil.getMessage("outbound.common.short.login_complete",input));
        }
    }
}
