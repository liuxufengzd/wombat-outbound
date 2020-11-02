package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import com.rakuten.ecld.wms.wombatoutbound.entity.BadItem;
import com.rakuten.ecld.wms.wombatoutbound.service.command.common.BadItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class QAProcess extends AbstractBaseStepHandler<PtgState> {
    private final BadItemService badItemService;

    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> QA process");

        if (cliHandler.getState().getNumberExcludeBadItem() > 0){
            List<BadItem> badItems = badItemService.findAllBadItems();
            badItems.forEach(badItem -> {
                cliHandler.response("間口ー箱ラベル: "+badItem.getBoxArea()+"-"+badItem.getBoxLabel());
                cliHandler.response("納品書番号： "+badItem.getDeliveryCode());
                cliHandler.response("商品コート: "+badItem.getItemCode());
                String type = badItem.getType();
                if ("short".equals(type))
                    cliHandler.response("問題: 欠品");
                if ("damage".equals(type))
                    cliHandler.response("問題: ダメージ");
            });
        }
    }
}
