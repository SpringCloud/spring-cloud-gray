package cn.springcloud.test;

import cn.springcloud.gray.eureka.GrayEruekaApplication;
import cn.springcloud.gray.eureka.domain.ApiRes;
import cn.springcloud.gray.utils.ConfigurationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableBiMap;
import lombok.Data;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActiveProfiles({"dev"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrayEruekaApplication.class)
public class Test {


    @Autowired
    private ApplicationContext cxt;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private Validator validator;

    @org.junit.Test
    public void testBind() {
        Map<String, String> args = ImmutableBiMap.of("s", "10", "adf", "a,b,c,d", "apiRes", "#{@apiRes}");
        SpelExpressionParser parser = new SpelExpressionParser();

        Map<String, Object> properties = ConfigurationUtils.normalize(args, parser, cxt);


        Object configuration = new St();

        ConfigurationUtils.bind(configuration, properties,
                "", "", validator,
                conversionService);
        System.out.println(configuration);


    }


    @Data
    public static class St {
        private int s;
        private List<String> adf;
        private ApiRes apiRes;


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
