package com.rakuten.ecld.wms.wombatoutbound.architecture.core;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.BaseState;
import com.rakuten.ecld.wms.wombatoutbound.architecture.common.Condition;
import com.rakuten.ecld.wms.wombatoutbound.architecture.common.StepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.CliHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response.CliParams;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The modelRunner is newly defined by every thread of the application for thread security.
 * ModelRunner records the status of a transaction and trace the running trajectory.
 *
 * @author Xufeng Liu
 * @see Flow
 * @see Model
 */
public class ModelRunner {
    private final CliHandler cliHandler;
    private final String latestStepName;
    private Step latestStep;
    private Step stepPosition;
    private int consumeIndex = 2;
    private boolean skipFlag;
    private boolean endFlag;
    private Map<String, String> flowToRootStep;
    private Map<String, String> flowToCallerStep;

    public ModelRunner(CliHandler cliHandler, String latestStepName) {
        this.cliHandler = cliHandler;
        this.cliHandler.setModelRunner(this);
        this.latestStepName = latestStepName;
        if (cliHandler.getState() instanceof BaseState) {
            flowToRootStep = ((BaseState) cliHandler.getState()).getFlowToRootStep();
            flowToCallerStep = ((BaseState) cliHandler.getState()).getFlowToCallerStep();
        }
    }

    public CliHandler getCliHandler() {
        return cliHandler;
    }

    public String getLatestStepName() {
        return latestStep.getName();
    }

    /**
     * Define the process to run the model start from the indicated step.
     *
     * @param model the model to run
     */
    public void run(Model model) {
        Step stepToRun;
        if (latestStepName == null) {
            stepToRun = model.getMainFlow().getFirstStep();
        } else {
            latestStep = findLatestStep(model, this.latestStepName);
            if (latestStep == null)
                throw new RuntimeException("Cannot find the step: " + this.latestStepName);
            stepPosition = latestStep;
            stepToRun = findNextStepToRun();
        }
        while (true) {
            if (stepToRun != null) {
                if (stepToRun.isConsume())
                    consumeIndex--;
                if (consumeIndex > 0) {
                    runStep(stepToRun);
                } else break;
                if (cliHandler.isFailed()) {
                    String failToJump = stepToRun.getFailToJump();
                    Step repeatStep = failToJump == null ? latestStep : model.findStep(failToJump);
                    if (repeatStep == null)
                        throw new RuntimeException("Cannot find the step: " + failToJump);
                    cliHandler.setFailed(false);
                    // consume flag of this step will be ignored
                    runStep(repeatStep);
                    if (stepToRun.isBreakMode()){
                        break;
                    }else {
                        stepToRun = findNextStepToRun();
                        continue;
                    }
                }
                if (stepToRun.isContinueAtRootFlag() && !skipFlag) {
                    String flowName = stepToRun.getFlow().getFlowName();
                    stepToRun = model.findStep(flowToRootStep.get(flowName));
                    flowToRootStep.remove(flowName);
                } else stepToRun = findNextStepToRun();
            } else {
                endFlag = true;
                break;
            }
        }
        CliParams cliParams = latestStep.getCliParams();
        cliHandler.setCliParams(cliParams != null ? cliParams : new CliParams());
    }

    /**
     * Try to execute a step and record the step position that has been run
     *
     * @param step the step to execute
     */
    private void runStep(Step step) {
        Condition condition = step.getCondition();
        if ((condition == null || condition.evaluate(cliHandler)) &&
                (step.getCaller().isEmpty() || step.getCaller().equals(flowToCallerStep.get(step.getFlow().getFlowName())))) {
            skipFlag = false;
            StepHandler stepHandler = step.getStepHandler();
            if (stepHandler != null)
                stepHandler.execute(cliHandler);
            if (!cliHandler.isFailed())
                latestStep = step;
        } else skipFlag = true;
        stepPosition = step;
    }

    /**
     * Find the next step to execute after executing the current step.
     * The next step may be directly after the current step in the same flow,
     * or in another flow that is defined after the current step, or the
     * step that is defined in the continueAt method.
     *
     * @return the step to run
     */
    private Step findNextStepToRun() {
        if (!skipFlag && stepPosition.getFirstChoice() != null) {
            Step nextStep = stepPosition.getFirstChoice();
            if (cliHandler.getState() instanceof BaseState)
                updateBaseState(stepPosition, nextStep);
            return nextStep;
        }
        List<Flow> afterFlows = stepPosition.getAfterFlows();
        if (afterFlows.isEmpty())
            return stepPosition.getNextStep();
        Optional<Flow> nextFlow = afterFlows.stream().filter(flow -> flow.getCondition().evaluate(cliHandler)).findFirst();
        if (nextFlow.isEmpty())
            return stepPosition.getNextStep();
        Flow chosenFlow = nextFlow.get();
        Step firstStep = chosenFlow.getFirstStep();
        if (cliHandler.getState() instanceof BaseState)
            updateBaseState(stepPosition, firstStep);
        if (chosenFlow.isConsume())
            consumeIndex--;

        return firstStep;
    }

    private Step findLatestStep(Model model, String stepName) {
        return model.findStep(stepName);
    }

    public boolean isEndFlag() {
        return endFlag;
    }

    private void updateBaseState(Step caller, Step callee) {
        if (caller.isRootStep()) {
            flowToRootStep.put(callee.getFlow().getFlowName(), caller.getName());
        } else {
            String flowName = caller.getFlow().getFlowName();
            String rootStep = flowToRootStep.get(flowName);
            if (rootStep != null) {
                flowToRootStep.remove(flowName);
                flowToRootStep.put(callee.getFlow().getFlowName(), rootStep);
            }
        }
        if (callee.getFlow().isCallerFlag())
            flowToCallerStep.put(callee.getFlow().getFlowName(), caller.getName());
        if (caller.getFlow().isCallerFlag() && (caller.getFlow() != callee.getFlow()))
            flowToCallerStep.remove(caller.getFlow().getFlowName());
    }
}
