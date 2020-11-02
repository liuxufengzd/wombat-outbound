package com.rakuten.ecld.wms.wombatoutbound.service.command.picktogo.Impl;

import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import com.rakuten.ecld.wms.wombatoutbound.entity.Batch;
import com.rakuten.ecld.wms.wombatoutbound.entity.Item;
import com.rakuten.ecld.wms.wombatoutbound.exception.BusinessException;
import com.rakuten.ecld.wms.wombatoutbound.mapper.DeliveryMapper;
import com.rakuten.ecld.wms.wombatoutbound.service.command.picktogo.PtgDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PtgDeliveryServiceImpl implements PtgDeliveryService {
    private final DeliveryMapper deliveryMapper;

    @Override
    public void batchEstimate(CliHandler<PtgState> cliHandler) {
        String deliveryCode = cliHandler.getInput();
        PtgState ptgState = cliHandler.getState();

        // for test
        Batch batch = deliveryMapper.findBatch(deliveryCode);
        if (batch == null)
            throw new BusinessException("outbound.common.delivery_problem");
        if (batch.getRFlag() == 1)
            ptgState.setBatchRegistered(true);
        if (batch.getCFlag() == 1)
            ptgState.setPickFinished(true);
        ptgState.setBatch(batch.getCode());
    }

    @Override
    public Item findNextItemToPick(String batch) {
        return deliveryMapper.findNextItemToPick(batch);
    }

    @Override
    public List<String> getUnregisteredDeliveries(String batch) {
        return deliveryMapper.findUnregisteredDeliveries(batch);
    }

    @Override
    public void registerDelivery(String deliveryCode, String boxLabel, String boxArea) {
        deliveryMapper.registerDelivery(deliveryCode, boxLabel, boxArea);
    }

    @Override
    public void registerBatch(String batch) {
        deliveryMapper.registerBatch(batch);
    }

    @Override
    public void pickItem(String deliveryCode, String itemCode, int quantity) {
        deliveryMapper.pickItem(deliveryCode, itemCode, quantity);
    }

    @Override
    public boolean hasAllItemPicked(String deliveryCode) {
        return deliveryMapper.findUnpickedItem(deliveryCode) == 0;
    }

    @Override
    public boolean hasAllDeliveriesPicked(String batchCode) {
        return deliveryMapper.findUnpickedDelivery(batchCode) == 0;
    }

    @Override
    public void setDeliveryPicked(String deliveryCode) {
        deliveryMapper.setDeliveryPicked(deliveryCode);
    }

    @Override
    public void setBatchPicked(String batchCode) {
        deliveryMapper.setBatchPicked(batchCode);
    }

    @Override
    public String nextBoxArea(String boxArea) {
        if (boxArea == null)
            boxArea = "01";
        else {
            int nextArea = Integer.parseInt(boxArea) + 1;
            if (nextArea > 9)
                boxArea = Integer.toString(nextArea);
            else boxArea = "0" + nextArea;
        }
        return boxArea;
    }

    @Override
    public String lastBoxArea(String boxArea) {
        if (boxArea == null)
            boxArea = "01";
        int nextArea = Integer.parseInt(boxArea) - 1;
        if (nextArea > 9)
            boxArea = Integer.toString(nextArea);
        else boxArea = "0" + nextArea;
        return boxArea;
    }
}
