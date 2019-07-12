package cn.springcloud.test;

import cn.springcloud.gray.eureka.GrayEruekaApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableBiMap;
import lombok.Data;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Validator;

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

//        Map<String, Object> properties = ConfigurationUtils.normalize(args, parser, cxt);


//        Object configuration = new St();
//
//        ConfigurationUtils.bind(configuration, properties,
//                "", "", validator,
//                conversionService);
//        System.out.println(configuration);


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
