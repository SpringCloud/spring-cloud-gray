package cn.springcloud.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@ActiveProfiles({"dev"})
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = GrayEruekaApplication.class)
public class Test {


    @Autowired
    private ApplicationContext cxt;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private Validator validator;

    @org.junit.Test
    public void testBind() {
//        Map<String, String> args = ImmutableBiMap.of("s", "10", "adf", "a,b,c,d", "apiRes", "#{@apiRes}");
        Map<String, String> args = new HashMap<>();
        args.put("adf", "asdfd");
        SpelExpressionParser parser = new SpelExpressionParser();
        String expressionStr = "#args['adf']";
//        Expression expression = parser.parseExpression(expressionStr);


        EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        context.setVariable("args", args);

        Expression expression = parser.parseExpression(expressionStr);


        Object obj = expression.getValue(context);
        System.out.println(obj);

//        Map<String, Object> properties = ConfigurationUtils.normalize(args, parser, cxt);


//        Object configuration = new St();
//
//        ConfigurationUtils.bind(configuration, properties,
//                "", "", validator,
//                conversionService);
//        System.out.println(configuration);


//        LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
//        Method[] methods = St.class.getMethods();
//        for (Method method : methods) {
//            String[] methodParameterNames = parameterNameDiscoverer.getParameterNames(method);
//            System.out.println(StringUtils.join(methodParameterNames, ","));
//        }

    }


    @Data
    public static class St {
        private int s;
        private List<String> adf;


        @Override
        public String toString() {
            try {
                return new ObjectMapper().writeValueAsString(this);
            } catch (Exception e) {
                return null;
            }
        }
    }


}
