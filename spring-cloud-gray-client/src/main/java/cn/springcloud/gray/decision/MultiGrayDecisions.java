package cn.springcloud.gray.decision;


import java.util.ArrayList;
import java.util.List;

/**
 * 组合从个灰度策略
 */
public class MultiGrayDecisions implements GrayDecision {

    private List<GrayDecision> decisions;


    public MultiGrayDecisions() {
        this(new ArrayList<>());
    }

    public MultiGrayDecisions(List<GrayDecision> decisions) {
        this.decisions = decisions;
    }

    public MultiGrayDecisions and(GrayDecision other) {
        decisions.add(other);
        return this;
    }

    @Override
    public boolean test(DecisionInputArgs args) {
        for (GrayDecision decision : decisions) {
            if (!decision.test(args)) {
                return false;
            }
        }
        return true;
    }


}
