package cn.springcloud.gray.decision;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author saleson
 * @date 2020-04-07 22:15
 */
@Data
@Accessors(chain = true)
public class Policy {

    private String id;

    private List<GrayDecision> decisions;


    public boolean predicateDecisions(DecisionInputArgs args) {
        if (decisions.isEmpty()) {
            return false;
        }
        for (GrayDecision decision : decisions) {
            if (!decision.test(args)) {
                return false;
            }
        }
        return true;
    }

}
