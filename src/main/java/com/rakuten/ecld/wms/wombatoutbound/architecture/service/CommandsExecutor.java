package com.rakuten.ecld.wms.wombatoutbound.architecture.service;

import com.rakuten.ecld.wms.wombatoutbound.architecture.core.ModelRunner;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.ModelState;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request.HeaderInfo;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request.JWTUserDetail;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.request.RequestObject;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response.ResponseObject;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.FlowEndType;
import com.rakuten.ecld.wms.wombatoutbound.architecture.util.JWTTokenUtil;
import com.rakuten.ecld.wms.wombatoutbound.architecture.util.StatesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandsExecutor {
    private final ModelService modelService;
    private final StatesUtil statesUtil;
    private final JWTTokenUtil jwtTokenUtil;

    public ResponseObject execute(RequestObject request, HeaderInfo headerInfo) {
        // find the command model
        ModelState modelState = modelService.findModelForCommand(request.getProcess());
        // decode part
        CliHandler<?> cliHandler = buildCliHandler(request, headerInfo, modelState.getState());
        // command handling part
        ModelRunner runner = new ModelRunner(cliHandler, request.getStep());
        runner.run(modelState.getModel());
        // encode part
        String latestStep = runner.getLatestStepName();
        return buildResponse(request, latestStep, runner);
    }

    private CliHandler<?> buildCliHandler(RequestObject request, HeaderInfo headerInfo, Object state) {
        Object stateObject = statesUtil.deserializeState(request.getStateData(), state);
        JWTUserDetail userDetail = jwtTokenUtil.getUserFromToken(headerInfo.getToken());
        return CliHandler.builder()
                .state(stateObject)
                .input(request.getInput())
                .centerCode(userDetail.getCenter().iterator().next())
                .business(userDetail.getBusiness())
                .user(userDetail)
                .terminalId(headerInfo.getTerminalId())
                .sellerType(headerInfo.getSellerType())
                .build();
    }

    private ResponseObject buildResponse(RequestObject request, String latestStep, ModelRunner runner) {
        CliHandler<?> cliHandler = runner.getCliHandler();
        return ResponseObject.builder()
                .process(request.getProcess())
                .step(latestStep)
                .cli(cliHandler.getCliParams())
                .nextStep(!runner.isEndFlag())
                .stateData(statesUtil.serialize(cliHandler.getState()))
                .soundId(cliHandler.getSoundId())
                .response(cliHandler.getResponse())
                .logActivity(cliHandler.getLogActivity())
                .flowEndType(cliHandler.isReturn()? FlowEndType.RETURN : FlowEndType.END)
                .build();
    }
}
