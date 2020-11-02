package com.rakuten.ecld.wms.wombatoutbound.constant;

public final class CommandConstant {

    public static final String ROLE_VETERAN = "RB_GENERAL_MANAGERs";
    public static final Integer DELIVERY_CODE_MAX = 25;
    public static final Integer SHELF_CODE_MAX = 13;
    public static final Integer ITEM_CODE_MAX = 20;
    public static final Integer BOX_LABEL_MAX = 14;
    public static final Integer NUMBER_MAX = 10;
    // for test
    public static final String DELIVERY_CODE_REGEX= "^.{2,5}$";
    public static final String BOX_LABEL_REGEX = "^.{3,10}$";
    public static final String SHELF_CODE_REGEX = "^.{2}-.{1}-.{1}$";
    public static final String ITEM_CODE_REGEX = "^.{1,10}$";
    public static final String POSITIVE_INTEGER_REGEX = "[1-9][0-9]*";
    public static final String INTEGER_REGEX = "[0-9]+";

    private CommandConstant() {
        throw new AssertionError("Sorry! I can't instantiate this class for you.");
    }
}
