package cn.springcloud.gray.decision;

/**
 * @author saleson
 * @date 2020-07-28 19:19
 */
public class GrayDecisionDelegater implements GrayDecision {

    private GrayDecision delegate;

    public GrayDecisionDelegater(GrayDecision delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean test(GrayDecisionInputArgs args) {
        return delegate.test(args);
    }
}
