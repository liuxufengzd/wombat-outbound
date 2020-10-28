package com.rakuten.ecld.wms.wombatoutbound.architecture.core;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.Condition;
import com.rakuten.ecld.wms.wombatoutbound.architecture.common.EmptyHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.common.EndConfirmQuestion;
import com.rakuten.ecld.wms.wombatoutbound.architecture.common.StepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.domain.response.CliParams;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.Action;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.InputFormat;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.InputType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Class used to define the step to run.
 *
 * @author Xufeng Liu
 * @see Flow
 * @see Model
 */
public class Step {
    private final String stepName;
    private final Model model;
    private final Flow flow;
    private final List<Flow> afterFlows = new ArrayList<>();
    private final boolean consume;
    private final Condition<?> condition;
    private Step firstChoice;
    private Step nextStep;
    private StepHandler<?> stepHandler;
    private CliParams cliParams;
    private boolean continueAtRootFlag;
    private String caller = "";
    private final boolean rootStep;

    public Step(String stepName, Model model, Flow flow, Condition<?> condition, boolean consume, boolean rootStep) {
        this.stepName = stepName;
        this.model = model;
        this.flow = flow;
        this.condition = condition;
        this.consume = consume;
        this.rootStep = rootStep;
    }

    StepHandler<?> getStepHandler() {
        return stepHandler;
    }

    Condition<?> getCondition() {
        return condition;
    }

    String getName() {
        return stepName;
    }

    String getCaller() {
        return caller;
    }

    boolean isRootStep() {
        return rootStep;
    }

    Step getNextStep() {
        return nextStep;
    }

    Step getFirstChoice() {
        return firstChoice;
    }

    CliParams getCliParams() {
        return cliParams;
    }

    Flow getFlow() {
        return flow;
    }

    boolean isContinueAtRootFlag() {
        return continueAtRootFlag;
    }

    void setFirstChoice(Step firstChoice) {
        this.firstChoice = firstChoice;
    }

    void setNextStep(Step nextStep) {
        this.nextStep = nextStep;
    }

    boolean isConsume() {
        return consume;
    }

    List<Flow> getAfterFlows() {
        return afterFlows;
    }

    void addAfterFlow(Flow flow) {
        afterFlows.add(flow);
    }

    private void generateCliParam() {
        if (cliParams == null)
            cliParams = new CliParams();
    }

    /**
     * Run the step
     *
     * @param handlerType class that defines the process
     * @return the flow contains this step
     */
    public Flow run(Class<? extends StepHandler> handlerType) {
        stepHandler = model.getHandlerFactory().createStepHandler(handlerType);
        return flow;
    }

    /**
     * Indicate which step will be executed after executing this step
     *
     * @param stepName the name of the next step
     * @return the flow contains this step
     */
    public Flow continueAt(String stepName) {
        Step step = model.findStep(stepName);
        if (step == null) {
            model.getUnMappedSteps().put(this, stepName);
        } else firstChoice = step;
        return flow;
    }

    /**
     * Jump to the root step recorded that is mapped to the flow of the step
     *
     * @return the flow contains this step
     */
    public Flow continueAtRoot() {
        continueAtRootFlag = true;
        flow.setContinueAtRootFlag();
        return flow;
    }

    /**
     * Execute the step when the name of the caller step called this flow equals callerStepName
     *
     * @param callerStepName the name of the caller step
     * @return this step
     */
    public Step callerIs(String callerStepName) {
        caller = callerStepName;
        flow.setCallerFlag();
        return this;
    }

    public Flow runAndContinueAtRoot(Class<? extends StepHandler> handlerType) {
        run(handlerType);
        continueAtRoot();
        return flow;
    }

    public Flow runAndContinueAt(Class<? extends StepHandler> handlerType, String stepName) {
        run(handlerType);
        continueAt(stepName);
        return flow;
    }

    public Step canEnd() {
        return canEnd(EmptyHandler.class, stepName);
    }

    public Step canEnd(Class<? extends StepHandler> handlerType) {
        return canEnd(handlerType, stepName);
    }

    public Step canEnd(String continueAt) {
        return canEnd(EmptyHandler.class, continueAt);
    }

    /**
     * Defines the end process of the step.
     *
     * @param handlerType the handler that will be executed if "end" is inputted
     * @param continueAt  which step to run if the end process is cancelled
     * @return this step
     */
    public Step canEnd(Class<? extends StepHandler> handlerType, String continueAt) {
        allowShortcut(Action.END);
        flow.flow(stepName + "-end", true).after(c -> "end".equals(c.getInput()), this.getName())
                .step(stepName + "-end").size(1).inputType(InputType.ALPHABETIC).allowShortcut(Action.YES, Action.NO).run(EndConfirmQuestion.class)
                .step(stepName + "-end-yn-question", c -> {
                    boolean result = "y".equals(c.getInput()) || "n".equals(c.getInput());
                    if (!result) {
                        c.fail("y/nで入力してください");
                        c.response("y/n？");
                    }
                    return result;
                }, true).run(handlerType)
                .step(stepName + "-end-cancel", c -> "n".equals(c.getInput())).continueAt(continueAt);
        return this;
    }

    public Step size(int max) {
        return size(1, max);
    }

    public Step size(int min, int max) {
        if (min < 0 || max < min)
            throw new RuntimeException("Invalid size initialization");
        generateCliParam();
        cliParams.setMax(min);
        cliParams.setMax(max);
        return this;
    }

    public Step inputType(InputType inputType) {
        generateCliParam();
        cliParams.setInputType(inputType);
        return this;
    }

    public Step commandCanBeChanged() {
        generateCliParam();
        cliParams.setChangeCommand(true);
        return this;
    }

    public Step screenCanBeCleared() {
        generateCliParam();
        cliParams.setClearScreen(true);
        return this;
    }

    public Step format(InputFormat format) {
        generateCliParam();
        cliParams.setFormat(format);
        return this;
    }

    public Step allowShortcut(Action... actions) {
        generateCliParam();
        Action[] actionKey = cliParams.getActionKey();
        if (actionKey != null) {
            List<Action> actionList = Arrays.asList(actionKey);
            actionList = new ArrayList<>(actionList);
            actionList.addAll(Arrays.asList(actions));
            HashSet<Action> actionHashSet = new HashSet<>(actionList);
            cliParams.setActionKey(actionHashSet.toArray(new Action[0]));
        } else cliParams.setActionKey(actions);
        return this;
    }
}
