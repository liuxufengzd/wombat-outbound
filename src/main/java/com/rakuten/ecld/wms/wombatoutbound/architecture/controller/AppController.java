package com.rakuten.ecld.wms.wombatoutbound.architecture.controller;

import com.rakuten.ecld.wms.wombatoutbound.architecture.constant.V1Plus;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request.RequestObject;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response.ResponseObject;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response.ServiceCommandObject;
import com.rakuten.ecld.wms.wombatoutbound.architecture.service.CommandExecutor;
import com.rakuten.ecld.wms.wombatoutbound.architecture.service.ModelService;
import com.rakuten.ecld.wms.wombatoutbound.architecture.util.JWTTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class AppController {
    private final ModelService modelService;
    private final CommandExecutor executor;
    private final JWTTokenUtil jwtTokenUtil;
    @Value("${spring.application.name}")
    private String serviceName;

    @GetMapping(V1Plus.Endpoints.TEST)
    public String testOutbound() {
        return "Hello " + serviceName;
    }

    @GetMapping(V1Plus.Endpoints.COMMANDS)
    public ServiceCommandObject scanCommand() {
        List<ServiceCommandObject.Command> commands = modelService.getCommandList();
        ServiceCommandObject commandObject = new ServiceCommandObject();
        commandObject.setCommands(commands);
        return commandObject;
    }

    @PostMapping(V1Plus.Endpoints.RUN_COMMAND)
    public ResponseObject runCommand(@RequestBody RequestObject reqObj, @RequestHeader Map<String, String> headers) {
        reqObj.log();
        String token = jwtTokenUtil.getToken(headers);
        ResponseObject response = executor.execute(reqObj, token);
        log.info("Response: " + response);
        return response;
    }
}
