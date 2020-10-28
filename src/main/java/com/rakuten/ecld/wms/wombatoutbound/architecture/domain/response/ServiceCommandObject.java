package com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServiceCommandObject {
    private List<Command> commands;
    @Getter
    @Setter
    @Builder
    public static class Command {
        private String name;
        private String[] aliases;
    }
}
