package com.rakuten.ecld.wms.wombatoutbound.service.common.impl;

import com.rakuten.ecld.wms.wombatoutbound.entity.BadItem;
import com.rakuten.ecld.wms.wombatoutbound.entity.Item;
import com.rakuten.ecld.wms.wombatoutbound.mapper.BadItemMapper;
import com.rakuten.ecld.wms.wombatoutbound.service.common.BadItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BadItemServiceImpl implements BadItemService {
    private final BadItemMapper badItemMapper;

    @Override
    public void badItemLogin(Item item, String type, int number) {
        badItemMapper.addBadItem(item.getDeliveryCode(), item.getItemCode(), type, number);
    }

    @Override
    public void setBadItemFlag(Item item) {
        badItemMapper.setBadFlag(item.getDeliveryCode(), item.getItemCode());
    }

    @Override
    public int badItemNumber(Item item) {
        return badItemMapper.getBadItemNumber(item.getDeliveryCode(),item.getItemCode());
    }

    @Override
    public List<BadItem> findAllBadItems() {
        return badItemMapper.getBadItems();
    }

    @Override
    public void fixBadItem(String itemCode) {
        badItemMapper.removeBadFlag(itemCode);
        badItemMapper.fixBadItem(itemCode);
    }
}
