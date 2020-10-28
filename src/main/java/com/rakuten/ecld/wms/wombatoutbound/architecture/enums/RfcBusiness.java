package com.rakuten.ecld.wms.wombatoutbound.architecture.enums;

import org.apache.commons.lang3.StringUtils;

public enum RfcBusiness {
    RSL,
    BOOKS,
    RF;

    public static RfcBusiness getEnumByName(final String business) {

        if (StringUtils.isBlank(business)) {
            return null;
        }
        for (RfcBusiness values : RfcBusiness.values()) {
            if (values.name().equalsIgnoreCase(business)) {
                return values;
            }
        }
        return null;
    }
}
