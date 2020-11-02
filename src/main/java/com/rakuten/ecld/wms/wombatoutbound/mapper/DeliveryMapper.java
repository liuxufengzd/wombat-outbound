package com.rakuten.ecld.wms.wombatoutbound.mapper;

import com.rakuten.ecld.wms.wombatoutbound.entity.Batch;
import com.rakuten.ecld.wms.wombatoutbound.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeliveryMapper {
    Batch findBatch(@Param("code") String deliveryCode);
    List<String> findUnregisteredDeliveries(@Param("code") String batchCode);
    void registerDelivery(@Param("code") String deliveryCode, @Param("boxLabel") String boxLabel, @Param("boxArea") String boxArea);
    void registerBatch(@Param("code") String batchCode);
    Item findNextItemToPick(@Param("code") String batchCode);
    void pickItem(@Param("deliveryCode") String deliveryCode, @Param("itemCode") String itemCode, @Param("quantity") int quantity);
    Integer findUnpickedItem(@Param("code") String deliveryCode);
    Integer findUnpickedDelivery(@Param("code") String batchCode);
    void setDeliveryPicked(@Param("code") String deliveryCode);
    void setBatchPicked(@Param("code") String batchCode);
}
