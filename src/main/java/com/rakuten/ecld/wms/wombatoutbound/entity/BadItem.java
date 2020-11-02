package com.rakuten.ecld.wms.wombatoutbound.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BadItem {
    private String boxArea;
    private String boxLabel;
    private String deliveryCode;
    private String itemCode;
    private int number;
    private String type;
}
