package cn.springcloud.gray.decision;


/**
 * 该接口是灰度决策，用来判断请求是否匹配灰度策略。
 * 实现了ip匹配、request parameter匹配、request header匹配、BambooRequestContext中的参数匹配器以及合并匹配等多个匹配能力。
 */
public interface GrayDecision {


    boolean test(GrayDecisionInputArgs args);


    static AllowGraydecision allow() {
        return AllowGraydecision.INSTANCE;
    }


    static RefuseGraydecision refuse() {
        return RefuseGraydecision.INSTANCE;
    }


    /**
     * 默认允许
     */
    class AllowGraydecision implements GrayDecision {


        private static AllowGraydecision INSTANCE = new AllowGraydecision();

        private AllowGraydecision() {

        }


        @Override
        public boolean test(GrayDecisionInputArgs args) {
            return true;
        }
    }


    /**
     * 默认拒绝
     */
    class RefuseGraydecision implements GrayDecision {


        private static RefuseGraydecision INSTANCE = new RefuseGraydecision();

        private RefuseGraydecision() {

        }

        @Override
        public boolean test(GrayDecisionInputArgs args) {
            return false;
        }
    }

}
