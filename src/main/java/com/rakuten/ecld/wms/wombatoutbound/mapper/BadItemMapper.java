package com.rakuten.ecld.wms.wombatoutbound.mapper;

import com.rakuten.ecld.wms.wombatoutbound.entity.BadItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BadItemMapper {
    void setBadFlag(@Param("deliveryCode") String deliveryCode, @Param("itemCode") String itemCode);
    void removeBadFlag(@Param("itemCode") String itemCode);
    void addBadItem(@Param("deliveryCode") String deliveryCode, @Param("itemCode") String itemCode, @Param("type") String type, @Param("number") int number);
    int getBadItemNumber(@Param("deliveryCode") String deliveryCode, @Param("itemCode") String itemCode);
    List<BadItem> getBadItems();
    void fixBadItem(@Param("itemCode") String itemCode);
}
