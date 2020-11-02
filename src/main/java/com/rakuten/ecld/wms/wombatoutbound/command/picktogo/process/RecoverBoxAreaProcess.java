package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import com.rakuten.ecld.wms.wombatoutbound.entity.Item;
import com.rakuten.ecld.wms.wombatoutbound.service.command.picktogo.PtgDeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RecoverBoxAreaProcess extends AbstractBaseStepHandler<PtgState> {
    private final PtgDeliveryService ptgDeliveryService;

    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> recover box area process");

        if (cliHandler.getModelRunner().getLatestStepName().equals("box-label-question")){
            Item item = cliHandler.getState().getItem();
            item.setBoxArea(ptgDeliveryService.lastBoxArea(item.getBoxArea()));
        }
    }
}
