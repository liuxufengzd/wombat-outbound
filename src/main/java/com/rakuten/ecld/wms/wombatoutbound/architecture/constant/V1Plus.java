package com.rakuten.ecld.wms.wombatoutbound.architecture.constant;

public class V1Plus {
    public static final String PATH = "/api/v{apiVersion:[1-9]\\d*}";

    public static class Endpoints {
        public static final String TEST = PATH + "/test";
        public static final String COMMANDS = PATH + "/commands";
        public static final String RUN_COMMAND = PATH + "/run-command";
        private Endpoints() {}
    }

    private V1Plus() {}
}
