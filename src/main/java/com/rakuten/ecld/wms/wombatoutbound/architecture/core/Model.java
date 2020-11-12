package com.rakuten.ecld.wms.wombatoutbound.architecture.core;

import com.rakuten.ecld.wms.wombatoutbound.architecture.common.StepHandlerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used to define the model of a certain command.
 *
 * @author Xufeng Liu
 * @see Step
 * @see Flow
 */
public class Model {
    private final List<Flow> flows = new ArrayList<>();
    private final Map<Step, String> unMappedSteps = new HashMap<>();
    private final StepHandlerFactory stepHandlerFactory;

    public Model(StepHandlerFactory stepHandlerFactory) {
        this.stepHandlerFactory = stepHandlerFactory;
    }

    Flow getMainFlow() {
        return flows.get(0);
    }

    Map<Step, String> getUnMappedSteps() {
        return unMappedSteps;
    }

    StepHandlerFactory getStepHandlerFactory() {
        return stepHandlerFactory;
    }

    void addFlow(Flow flow) {
        flows.add(flow);
    }

    Step findStep(String stepName) {
        for (Flow flow : flows)
            for (Step step : flow.getSteps())
                if (step.getName().equals(stepName))
                    return step;
        return null;
    }

    /**
     * Define the main flow of the model. It indicates the position of the step to run at the beginning.
     * When a new transaction is accepted, the first step of this main flow
     * will be executed.
     *
     * @return the main flow
     */
    public Flow mainFlow() {
        Flow mainFlow = new Flow("main-flow", this, false);
        this.addFlow(mainFlow);
        return mainFlow;
    }
}
