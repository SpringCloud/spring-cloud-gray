package cn.springcloud.gray.decision;


/**
 * 组合从个灰度策略
 */
public class MultiGrayDecision implements GrayDecision {

    private GrayDecision decision;


    public MultiGrayDecision(GrayDecision decision) {
        this.decision = decision;
    }


    public MultiGrayDecision and(GrayDecision other) {
        GrayDecision cur = decision;
        decision = t -> cur.test(t) && other.test(t);
        return this;
    }

    @Override
    public boolean test(GrayDecisionInputArgs args) {
        return decision.test(args);
    }


}
