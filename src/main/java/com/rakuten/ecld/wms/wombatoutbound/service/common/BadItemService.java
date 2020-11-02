package com.rakuten.ecld.wms.wombatoutbound.service.common;

import com.rakuten.ecld.wms.wombatoutbound.entity.BadItem;
import com.rakuten.ecld.wms.wombatoutbound.entity.Item;

import java.util.List;

public interface BadItemService {
    void badItemLogin(Item item, String type, int number);
    void setBadItemFlag(Item item);
    int badItemNumber(Item item);
    List<BadItem> findAllBadItems();
    void fixBadItem(String itemCode);
}
