package com.rakuten.ecld.wms.wombatoutbound.command.picktogo.model;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.BaseState;
import com.rakuten.ecld.wms.wombatoutbound.entity.Item;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PtgState extends BaseState {
    private long startTime;
    private boolean batchRegistered;
    private boolean pickFinished;
    private String batch;
    private int pickNumber;
    private int pickedNumber;
    private int numberExcludeBadItem;
    private Item item = new Item();
}
