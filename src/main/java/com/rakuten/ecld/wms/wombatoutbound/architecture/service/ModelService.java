package com.rakuten.ecld.wms.wombatoutbound.architecture.service;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.CommandHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.ModelState;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response.ServiceCommandObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModelService implements ApplicationRunner {
    private List<CommandHandler> commandHandlers;
    @Autowired
    public void setCommandHandlers(List<CommandHandler> commandHandlers) {
        this.commandHandlers = commandHandlers;
    }

    @Override
    public void run(ApplicationArguments args) {
        commandHandlers.forEach(CommandHandler::define);
    }

    public ModelState findModelForCommand(String command){
        for (CommandHandler commandHandler:commandHandlers){
            String[] alias = commandHandler.getAlias();
            if (command.equals(commandHandler.getCommand()) ||
                    (alias!=null && Arrays.asList(alias).contains(command)))
                return new ModelState(commandHandler.getModel(),commandHandler.generateState());
        }
        return null;
    }

    public List<ServiceCommandObject.Command> getCommandList() {
        return this.commandHandlers.stream()
                .map(h -> ServiceCommandObject.Command
                        .builder()
                        .name(h.getCommand())
                        .aliases(h.getAlias())
                        .build())
                .collect(Collectors.toList());
    }
}
