package cn.springcloud.gray.client.plugin.dynamic.decsion;

import cn.springcloud.gray.client.plugin.dynamiclogic.FmDynamicLogicDriver;
import cn.springcloud.gray.client.plugin.dynamiclogic.GrayDecisionDynamicManager;
import cn.springcloud.gray.decision.DecisionInputArgs;
import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.GrayDecisionInputArgs;
import cn.springcloud.gray.decision.factory.GrayDecisionFactory;
import cn.springcloud.gray.dynamic.decision.DynamicGrayDecisionFactoryKeeper;
import cn.springcloud.gray.dynamiclogic.DynamicArgs;
import cn.springcloud.gray.dynamiclogic.DynamicLogicDefinition;
import cn.springcloud.gray.dynamiclogic.DynamicLogicDriver;
import cn.springcloud.gray.dynamiclogic.DynamicLogicManager;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.servernode.ServerSpec;
import com.fm.compiler.dynamicbean.DefaultDynamicBeanManager;
import com.fm.compiler.dynamicbean.DynamicBeanManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author saleson
 * @date 2019-12-28 17:17
 */
public class DynamicGrayDecisionFactoryKeeperTest {

    private DynamicGrayDecisionFactoryKeeper dynamicGrayDecisionFactoryKeeper;
    private DynamicLogicManager dynamicLogicManager;

    @Before
    public void before() {
        DynamicBeanManager dynamicBeanManager = new DefaultDynamicBeanManager();
        DynamicLogicDriver dynamicLogicDriver = new FmDynamicLogicDriver(dynamicBeanManager);
        dynamicLogicManager = new GrayDecisionDynamicManager(dynamicLogicDriver);
        dynamicGrayDecisionFactoryKeeper = new DynamicGrayDecisionFactoryKeeper(
                dynamicLogicManager, DefaultConversionService.getSharedInstance(), new Validator() {

            @Override
            public boolean supports(Class<?> clazz) {
                return false;
            }

            @Override
            public void validate(Object target, Errors errors) {

            }
        }, new ArrayList<>());
    }


    @Test
    public void test() {
        String decisionName = "Test";
        DynamicLogicDefinition dynamicLogicDefinition = new DynamicLogicDefinition();
        dynamicLogicDefinition.setName(decisionName + "GrayDecisionFactory");
        dynamicLogicDefinition.setLanguage("java");
        dynamicLogicDefinition.setCode("import cn.springcloud.gray.decision.GrayDecision;\n" +
                "import cn.springcloud.gray.decision.factory.AbstractGrayDecisionFactory;\n" +
                "import cn.springcloud.gray.dynamiclogic.DynamicStringArgs;\n" +
                "\n" +
                "/**\n" +
                " * @author saleson\n" +
                " * @date 2019-12-28 17:29\n" +
                " */\n" +
                "public class TestGrayDecisionFactory extends AbstractGrayDecisionFactory<DynamicStringArgs> {\n" +
                "\n" +
                "    public TestGrayDecisionFactory() {\n" +
                "        super(DynamicStringArgs.class);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public GrayDecision apply(DynamicStringArgs configBean) {\n" +
                "        return args -> {\n" +
                "            System.out.println(configBean);\n" +
                "            System.out.println(args.getGrayRequest());\n" +
                "            System.out.println(args.getServer());\n" +
                "            return true;\n" +
                "        };\n" +
                "    }\n" +
                "}");

        dynamicLogicManager.compleAndRegister(decisionName, dynamicLogicDefinition);


        GrayDecisionFactory grayDecisionFactory = dynamicGrayDecisionFactoryKeeper.getDecisionFactory(decisionName);
        GrayDecision grayDecision = grayDecisionFactory.apply(args -> System.out.println(args.getClass()));

        GrayRequest grayRequest = new GrayRequest();
        grayRequest.setServiceId("b");
        grayRequest.setUri(URI.create("lb://url?p=1"));

        DecisionInputArgs args = new GrayDecisionInputArgs()
                .setServer(ServerSpec.builder()
                        .instanceId("b-a")
                        .serviceId("b")
                        .metadatas(new HashMap<>())
                        .build())
                .setGrayRequest(grayRequest);
        boolean result = grayDecision.test(args);
        System.out.println(result);
    }


    @Test
    public void test2() throws ClassNotFoundException {
        DynamicArgs<String, String> dynamicArgs = new DynamicArgs<>();
        Class<DynamicArgs<String, String>> cls = (Class<DynamicArgs<String, String>>) new DynamicArgs<>().getClass();

        System.out.println(cls);
    }
}
