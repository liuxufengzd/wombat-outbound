package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import com.rakuten.ecld.wms.wombatoutbound.service.common.BadItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DamageCompleteProcess extends AbstractBaseStepHandler<PtgState> {
    private final BadItemService badItemService;

    @Override public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> damage complete");

        String input = cliHandler.getInput();
        int numberExcludeBadItem = cliHandler.getState().getNumberExcludeBadItem();
        if ("y".equals(input)){
            badItemService.badItemLogin(cliHandler.getState().getItem(), "damage", 1);
            cliHandler.getState().setNumberExcludeBadItem(numberExcludeBadItem - 1);
            cliHandler.response(messageSourceUtil.getMessage("outbound.common.damage.login_complete",new String[]{"1"}));
        }
        else {
            badItemService.badItemLogin(cliHandler.getState().getItem(), "damage", Integer.parseInt(input));
            cliHandler.getState().setNumberExcludeBadItem(numberExcludeBadItem - Integer.parseInt(input));
            cliHandler.response(messageSourceUtil.getMessage("outbound.common.damage.login_complete",new String[]{input}));
        }
        if (cliHandler.getState().getPickedNumber() == cliHandler.getState().getNumberExcludeBadItem()){
            badItemService.setBadItemFlag(cliHandler.getState().getItem());
        }
    }
}
