package com.rakuten.ecld.wms.wombatoutbound.architecture.service;

import com.rakuten.ecld.wms.wombatoutbound.architecture.core.ModelRunner;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.ModelState;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request.JWTUserDetail;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request.RequestObject;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response.ResponseObject;
import com.rakuten.ecld.wms.wombatoutbound.architecture.util.JWTTokenUtil;
import com.rakuten.ecld.wms.wombatoutbound.architecture.util.StateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandExecutor {
    private final ModelService modelService;
    private final StateUtil stateUtil;
    private final JWTTokenUtil jwtTokenUtil;

    public ResponseObject execute(RequestObject request, String token) {
        // find the command model
        ModelState modelState = modelService.findModelForCommand(request.getProcess());
        // decode part
        CliHandler<?> cliHandler = buildCliHandler(request, token, modelState.getState());
        // command handling part
        ModelRunner runner = new ModelRunner(cliHandler, request.getStep());
        runner.run(modelState.getModel());
        // encode part
        String latestStep = runner.getLatestStepName();
        return buildResponse(request, latestStep, runner);
    }

    private CliHandler<?> buildCliHandler(RequestObject request, String token, Object state) {
        Object stateObject = stateUtil.deserializeState(request.getStateData(), state);
        JWTUserDetail userDetail = jwtTokenUtil.getUserFromToken(token);
        return CliHandler.builder()
                .state(stateObject)
                .input(request.getInput())
                .centerCode(userDetail.getCenter().iterator().next())
                .business(userDetail.getBusiness())
                .user(userDetail)
                .build();
    }

    private ResponseObject buildResponse(RequestObject request, String latestStep, ModelRunner runner) {
        CliHandler<?> cliHandler = runner.getCliHandler();
        return ResponseObject.builder()
                .process(request.getProcess())
                .step(latestStep)
                .cli(cliHandler.getCliParams())
                .nextStep(!runner.isEndFlag())
                .stateData(stateUtil.serialize(cliHandler.getState()))
                .soundId(cliHandler.getSoundId())
                .response(cliHandler.getResponse())
                .logActivity(cliHandler.getLogActivity())
                .build();
    }
}
