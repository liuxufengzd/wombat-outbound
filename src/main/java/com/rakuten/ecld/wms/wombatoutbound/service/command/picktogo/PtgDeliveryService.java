package com.rakuten.ecld.wms.wombatoutbound.service.command.picktogo;

import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model.PtgState;
import com.rakuten.ecld.wms.wombatoutbound.entity.Item;

import java.util.List;

public interface PtgDeliveryService {
    void batchEstimate(CliHandler<PtgState> cliHandler);

    Item findNextItemToPick(String batch);

    List<String> getUnregisteredDeliveries(String batch);

    void registerDelivery(String deliveryCode,String boxLabel,String boxArea);

    void registerBatch(String batch);

    void pickItem(String deliveryCode, String itemCode,int quantity);

    boolean hasAllItemPicked(String deliveryCode);

    boolean hasAllDeliveriesPicked(String batchCode);

    void setDeliveryPicked(String deliveryCode);

    void setBatchPicked(String batchCode);

    String nextBoxArea(String boxArea);

    String lastBoxArea(String boxArea);
}
