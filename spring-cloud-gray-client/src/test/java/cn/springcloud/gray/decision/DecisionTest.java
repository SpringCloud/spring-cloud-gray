package cn.springcloud.gray.decision;

import cn.springcloud.gray.decision.compare.CompareMode;
import cn.springcloud.gray.decision.factory.HttpTrackParameterGrayDecisionFactory;
import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DecisionTest {

    private HttpTrackParameterGrayDecisionFactory trackParameterGrayDecisionFactory = new HttpTrackParameterGrayDecisionFactory();


    @Test
    public void test() {
        MultiGrayDecision decision = new MultiGrayDecision(GrayDecision.allow());
        Map<String, String> infos = new HashMap<>();
        infos.put("name", "version");
        infos.put("values", "4.0");
        infos.put("compareMode", "EQUAL");

        decision = decision.and(createGrayDecision("HttpTrackHeader", infos));

        infos.put("name", "platform");
        infos.put("values", "ios");

        decision = decision.and(createGrayDecision("HttpTrackHeader", infos));


        GrayHttpRequest grayHttpRequest = new GrayHttpRequest();
        GrayTrackInfo grayTrackInfo = new GrayTrackInfo();
        grayHttpRequest.setGrayTrackInfo(grayTrackInfo);
        grayTrackInfo.addParameter("version", "4.0");
        grayTrackInfo.addParameter("platform", "ios");
        DecisionInputArgs args = new GrayDecisionInputArgs().setGrayRequest(grayHttpRequest);

        TestTiming testTiming = new TestTiming();
        testTiming.start();
        for (int i = 0; i < 10000; i++) {
            boolean t = decision.test(args);
            testTiming.increaseTimes();
        }
        testTiming.stop();
        System.out.println(testTiming.result2map());


    }


    private GrayDecision createGrayDecision(String name, Map<String, String> infos) {
        return trackParameterGrayDecisionFactory.apply(configuration -> {
            configuration.setName(infos.get("name"));
            configuration.setValues(Arrays.asList(infos.get("values").split(",")));
            configuration.setCompareMode(CompareMode.valueOf(infos.get("compareMode")));
        });
    }

    private GrayDecision createGrayDecision(DecisionDefinition decisionDefinition) {
        return createGrayDecision(decisionDefinition.getName(), decisionDefinition.getInfos());

    }
}
