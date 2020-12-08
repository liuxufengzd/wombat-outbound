package com.rakuten.ecld.wms.wombatoutbound.architecture.controller;

import com.rakuten.ecld.wms.wombatclib.api.CommandHandler;
import com.rakuten.ecld.wms.wombatclib.domain.request.ClibRequestObject;
import com.rakuten.ecld.wms.wombatclib.domain.response.ClibResponseObject;
import com.rakuten.ecld.wms.wombatclib.service.CommandExecutor;
import com.rakuten.ecld.wms.wombatoutbound.architecture.constant.V1Plus;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request.HeaderInfo;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request.RequestObject;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response.ResponseObject;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response.ServiceCommandObject;
import com.rakuten.ecld.wms.wombatoutbound.architecture.service.CommandsExecutor;
import com.rakuten.ecld.wms.wombatoutbound.architecture.service.ModelService;
import com.rakuten.ecld.wms.wombatoutbound.architecture.util.JWTTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
// for old framework
@ComponentScan({"com.rakuten.ecld.wms.wombatclib.service","com.rakuten.ecld.wms.wombatclib.common.util"})
public class AppController {
    private final ModelService modelService;
    private final CommandsExecutor executor;
    private final JWTTokenUtil jwtTokenUtil;
    @Value("${spring.application.name}")
    private String serviceName;
    // for old framework
    private final List<CommandHandler> commandHandlers;
    private final CommandExecutor cliExecutor;

    @GetMapping(V1Plus.Endpoints.TEST)
    public String testOutbound() {
        return "Hello " + serviceName;
    }

    @GetMapping(V1Plus.Endpoints.COMMANDS)
    public ServiceCommandObject scanCommand() {
        List<ServiceCommandObject.Command> commands = modelService.getCommandList();
        ServiceCommandObject commandObject = new ServiceCommandObject();

        // for old framework
        commands.addAll(commandHandlers.stream()
            .map(h -> ServiceCommandObject.Command
                .builder()
                .name(h.getCommand())
                .aliases(h.getAlias())
                .build())
            .collect(Collectors.toList()));

        commandObject.setCommands(commands);
        return commandObject;
    }

    @PostMapping(V1Plus.Endpoints.RUN_COMMAND)
    public Object runCommand(@RequestBody RequestObject reqObj, @RequestHeader Map<String, String> headers) {
        reqObj.log();
        HeaderInfo headerInfo =
            new HeaderInfo(jwtTokenUtil.getToken(headers), headers.get("terminal-id"), headers.get("seller-type"));

        // for old framework
        if (reqObj.getProcess().equals("rac")){
            String token = jwtTokenUtil.getToken(headers);
            ClibRequestObject requestObject = new ClibRequestObject();
            requestObject.setInput(reqObj.getInput());
            requestObject.setProcess(reqObj.getProcess());
            requestObject.setStateData(reqObj.getStateData());
            requestObject.setStep(reqObj.getStep());
            ClibResponseObject responseObject = cliExecutor.execute(requestObject, token);
            log.info("response:"+requestObject);
            return responseObject;

        }else {
            ResponseObject response = executor.execute(reqObj, headerInfo);
            log.info("Response: " + response);
            return response;
        }
    }
}
