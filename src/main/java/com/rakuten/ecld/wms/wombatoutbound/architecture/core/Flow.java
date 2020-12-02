package com.rakuten.ecld.wms.wombatoutbound.architecture.core;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.*;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.Action;
import com.rakuten.ecld.wms.wombatoutbound.architecture.enums.InputType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Class used to define the process flow of a request.
 * It manages the relationship of the steps contained in this flow.
 *
 * @author Xufeng Liu
 * @see Step
 * @see Model
 */
public class Flow {
    private final String flowName;
    private final List<Step> steps = new ArrayList<>();
    private final Model model;
    private Condition condition;
    private final boolean consume;
    private boolean callerFlag;

    public Flow(String flowName, Model model, boolean consume) {
        this.flowName = flowName;
        this.model = model;
        this.consume = consume;
    }

    boolean isCallerFlag() {
        return callerFlag;
    }

    void setCallerFlag() {
        callerFlag = true;
    }

    private void setCondition(Condition condition) {
        this.condition = condition;
    }

    Condition getCondition() {
        return condition;
    }

    List<Step> getSteps() {
        return steps;
    }

    String getFlowName() {
        return flowName;
    }

    Step getFirstStep() {
        return steps.get(0);
    }

    boolean isConsume() {
        return consume;
    }

    public Step step(String stepName) {
        return step(stepName, null);
    }

    public Step step(String stepName, boolean consume) {
        return step(stepName, null, consume);
    }

    public Step step(String stepName, Condition condition) {
        return step(stepName, condition, false);
    }

    public Step step(String stepName, Condition condition, boolean consume) {
        return step(stepName, condition, consume, false);
    }

    public Step rootStep(String stepName) {
        return rootStep(stepName, null);
    }

    /**
     * Define a new root step. Root step is a special step which is recorded in the response when
     * it calls another flow. The root step will be passed to the third flow and be recorded.
     *
     * @param stepName  the name of the root step
     * @param condition the executing condition of the root step
     * @return new root step defined
     */
    public Step rootStep(String stepName, Condition condition) {
        return step(stepName, condition, false, true);
    }

    /**
     * Define a new step with name of stepName. The step can only be executed when the condition
     * is fulfilled. If the consume is true, the step will consume one request to execute. If the
     * request has been consumed once before, the process flow will finish before this step and return.
     * The rootFlag indicates whether the step is a root step or not.
     *
     * @param stepName  the name of the step
     * @param condition the executing condition of the step
     * @param consume   the consume flag
     * @param rootFlag  the root step flag
     * @return step newly defined
     */

    private Step step(String stepName, Condition condition, boolean consume, boolean rootFlag) {
        Step step = new Step(stepName, model, this, condition, consume, rootFlag);
        if (!steps.isEmpty())
            steps.get(steps.size() - 1).setNextStep(step);
        steps.add(step);
        return step;
    }

    public Flow YNStep(String ifN) {
        return YNStep(ifN, null);
    }

    public Flow YNStep(String ifN, boolean isInfo) {
        return YNStep(ifN, null, isInfo);
    }

    public Flow YNStep(String ifN, String ifY) {
        return YNStep(ifN, ifY, false);
    }

    /**
     * Ask the HT to input y/n.
     * IfN and ifY determine the step to continue at if "n/y" is inputted.
     * Null means continue at the next step of this YNStep.
     * If you should use continueAtRoot(), you need to pass "rootStep" into this YNStep.
     *
     * @param ifN if "n" is inputted then continue at the step.
     * @param ifY if "y" is inputted then continue at the step
     * @param isInfo indicate that if the y/n? question gets the response style of INFO
     * @return This flow
     */
    public Flow YNStep(String ifN, String ifY, boolean isInfo) {
        String name = steps.get(steps.size() - 1).getName();
        Flow ynFlow = step(name + "-ynStep0").
                size(1).inputType(InputType.ALPHABETIC).allowShortcut(
            Action.YES, Action.NO).run(isInfo ? YNQuestionInfo.class : YNQuestion.class)
                .step(name + "-ynStep1", true).run(YNEstimate.class);
        if (ifN != null) {
            if (ifN.equalsIgnoreCase("rootStep")) {
                ynFlow = ynFlow.step(name + "-ynStep2", c -> "n".equals(c.getInput())).continueAtRoot();
            } else ynFlow = ynFlow.step(name + "-ynStep2", c -> "n".equals(c.getInput())).continueAt(ifN);
        }
        if (ifY != null) {
            if (ifY.equalsIgnoreCase("rootStep")) {
                ynFlow.step(name + "-ynStep3", c -> "y".equals(c.getInput())).continueAtRoot();
            } else ynFlow.step(name + "-ynStep3", c -> "y".equals(c.getInput())).continueAt(ifY);
        }
        return this;
    }

    /**
     * Define a new flow of the model that contains the current flow.
     * If the consume flag is true, the flow will consume one request even
     * if the condition is not satisfied.
     *
     * @param flowName the name of the flow
     * @param consume  consume flag of the flow
     * @return newly defined flow
     */
    public Flow flow(String flowName, boolean consume) {
        Flow flow = new Flow(flowName, model, consume);
        model.addFlow(flow);
        return flow;
    }

    public Flow flow(String flowName) {
        return flow(flowName, false);
    }

    /**
     * The flow will try to be executed by runner after execution of
     * the inputted steps. If the condition is satisfied, the flow will
     * be executed.
     *
     * @param condition the condition of the flow
     * @param stepNames the name of the steps that after which the flow will try to be executed
     * @param <T>       state class of the cliHandler
     * @return this flow
     */
    public <T> Flow after(Condition<T> condition, String... stepNames) {
        setCondition(condition);
        Arrays.asList(stepNames).forEach(stepName -> {
            Step step = model.findStep(stepName);
            if (step == null)
                throw new RuntimeException("step not exist:" + stepName);
            step.addAfterFlow(this);
        });
        return this;
    }

    public Flow run(Class<? extends StepHandler> handlerType) {
        return step(flowName).run(handlerType);
    }

    public Flow runAndContinueAt(Class<? extends StepHandler> handlerType, String stepName) {
        return step(flowName).runAndContinueAt(handlerType, stepName);
    }

    /**
     * Set the next step to run just after this empty flow (no steps have been created in the flow)
     * Pay attention that you cannot use it directly after calling run() of any step. In place of it
     * please use continue() before calling run() of step or use runAndContinueAt()
     *
     * @param stepName the next step to run after executing this empty flow
     * @return this flow
     */
    public Flow continueAt(String stepName) {
        return step(flowName).continueAt(stepName);
    }

    public Model build() {
        Map<Step, String> unMappedSteps = model.getUnMappedSteps();
        if (!unMappedSteps.isEmpty()) {
            unMappedSteps.forEach((step, stepName) -> {
                Step firstChoice = model.findStep(stepName);
                if (firstChoice == null)
                    throw new RuntimeException("cannot find the step: " + stepName);
                step.setFirstChoice(firstChoice);
            });
        }
        return model;
    }
}
