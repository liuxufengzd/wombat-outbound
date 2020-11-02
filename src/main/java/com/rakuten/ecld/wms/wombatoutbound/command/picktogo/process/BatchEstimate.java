package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.AbstractBaseStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import com.rakuten.ecld.wms.wombatoutbound.service.command.picktogo.PtgDeliveryService;
import com.rakuten.ecld.wms.wombatoutbound.service.common.BadItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BatchEstimate extends AbstractBaseStepHandler<PtgState> {
    private final PtgDeliveryService ptgDeliveryService;
    private final BadItemService badItemService;

    @Override
    public void process(CliHandler<PtgState> cliHandler) {
        log.info("step --> batch estimate");

        PtgState state = cliHandler.getState();
        if (cliHandler.getState().getNumberExcludeBadItem() == 0){
            badItemService.setBadItemFlag(cliHandler.getState().getItem());
        }
        if (ptgDeliveryService.hasAllItemPicked(state.getItem().getDeliveryCode())){
            ptgDeliveryService.setDeliveryPicked(state.getItem().getDeliveryCode());
            if (ptgDeliveryService.hasAllDeliveriesPicked(state.getBatch())){
                ptgDeliveryService.setBatchPicked(state.getBatch());
                state.setPickFinished(true);
            }
        }
    }
}
