package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.ValueType;
import cn.springcloud.gray.decision.compare.PredicateComparator;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 慎用，基于SpEL实现，影响性能
 * 后面单独实例EL模块
 *
 * @author saleson
 * @date 2020-09-15 12:03
 */
@Slf4j
public class ExpressionGrayDecisionFactory extends CompareGrayDecisionFactory<ExpressionGrayDecisionFactory.Config> {

    private static final String GR = "gr";

    public ExpressionGrayDecisionFactory() {
        super(ExpressionGrayDecisionFactory.Config.class);
    }

    @Override
    public GrayDecision apply(Config configBean) {
        return args -> {
            GrayRequest grayRequest = args.getGrayRequest();
            if (Objects.equals(configBean.getValueType(), ValueType.SINGLE)) {
                return compareValue(configBean, grayRequest);
            }
            return compareValues(configBean, grayRequest);
        };
    }


    private boolean compareValue(Config configBean, GrayRequest grayRequest) {
        PredicateComparator<String> predicateComparator =
                getStringComparator(configBean.getCompareMode(), grayRequest.getServiceId());
        if (predicateComparator == null) {
            return false;
        }

        Object objValue = getValue(configBean.getValueExpression(), grayRequest);
        if (Objects.isNull(objValue)) {
            if (log.isDebugEnabled()) {
                log.debug("[ExpressionGrayDecisionFactory] - expression value is null serviceId:{}, decisionConfig:{}, grayRequest:{}, testReslut:{}",
                        grayRequest.getServiceId(), JsonUtils.toJsonString(configBean), grayRequest, false);
            }
            return false;
        }

        String value = objValue.toString();
        boolean b = predicateComparator.test(value, configBean.getValue());
        if (log.isDebugEnabled()) {
            log.debug("[ExpressionGrayDecisionFactory] serviceId:{}, decisionConfig:{}, attributeValues:{}, testReslut:{}",
                    grayRequest.getServiceId(), JsonUtils.toJsonString(configBean), value, b);
        }
        return b;
    }

    private boolean compareValues(Config configBean, GrayRequest grayRequest) {
        PredicateComparator<Collection<String>> predicateComparator =
                getCollectionStringComparator(configBean.getCompareMode(), grayRequest.getServiceId());
        if (predicateComparator == null) {
            return false;
        }

        Object objValue = getValue(configBean.getValueExpression(), grayRequest);
        if (Objects.isNull(objValue)) {
            if (log.isDebugEnabled()) {
                log.debug("[ExpressionGrayDecisionFactory] - expression value is null serviceId:{}, decisionConfig:{}, grayRequest:{}, testReslut:{}",
                        grayRequest.getServiceId(), JsonUtils.toJsonString(configBean), grayRequest, false);
            }
            return false;
        }

        List<String> values = new ArrayList<>();
        if (objValue.getClass().isArray()) {
            int length = Array.getLength(objValue);
            for (int i = 0; i < length; i++) {
                Object v = Array.get(objValue, i);
                if (Objects.nonNull(v)) {
                    values.add(v.toString());
                }
            }
        } else {
            values.add(objValue.toString());
        }

        boolean b = predicateComparator.test(values, configBean.getValues());
        if (log.isDebugEnabled()) {
            log.debug("[ExpressionGrayDecisionFactory] serviceId:{}, decisionConfig:{}, attributeValues:{}, testReslut:{}",
                    grayRequest.getServiceId(), JsonUtils.toJsonString(configBean), values, b);
        }
        return b;
    }


    private SpelExpressionParser parser = new SpelExpressionParser();

    private Object getValue(String valueExpression, GrayRequest grayRequest) {

        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable(GR, grayRequest);
        try {
            Expression expression = parser.parseExpression(valueExpression);

            Object obj = expression.getValue(context);
            return obj;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("SpelExpressionParser 获取数据失败", e);
            }
            System.out.println("SpelExpressionParser 获取数据失败:" + e.getMessage());
            return null;
        }
    }


//    private Object getValue(String valueExpression, GrayRequest grayRequest) {
//        JexlEngine jexl = new JexlBuilder().create();
//
//        // Create an expression
//        String jexlExp = "foo.innerFoo.bar()";
//        JexlExpression e = jexl.createExpression(jexlExp);
//
//        // Create a context and add data
//        JexlContext jc = new ObjectContext(jexl, grayRequest);
////        jc.set("foo", new Foo());
//
//        // Now evaluate the expression, getting the result
//        Object o = e.evaluate(jc);
//        return o;
//    }


    @Setter
    @Getter
    public static class Config extends CompareGrayDecisionFactory.CompareConfig {
        private String valueExpression;
        private ValueType valueType = ValueType.SINGLE;
        private List<String> values;
        private String value;

        public void setValues(List<String> values) {
            this.values = values;
            Collections.sort(values);
        }
    }
}
