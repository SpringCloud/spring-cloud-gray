package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.server.configuration.apidoc.PageableParameterAlternateTypeRuleConvention;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Created by saleson on 2017/7/6.
 */
@Configuration
@EnableSwagger2
//@Import(springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class)
@ComponentScan({"cn.springcloud.gray.server.resources"})
public class Swagger2Configuration extends WebMvcConfigurerAdapter {


//    @Bean
//    @Order(Ordered.LOWEST_PRECEDENCE)
//    public PageableParameterBuilderPlugin pageableParameterBuilderPlugin(
//            TypeNameExtractor nameExtractor, TypeResolver resolver) {
//        return new PageableParameterBuilderPlugin(nameExtractor, resolver);
//    }

    @Bean
    public PageableParameterAlternateTypeRuleConvention pageableParameterAlternateTypeRuleConvention(TypeResolver resolver) {
        return new PageableParameterAlternateTypeRuleConvention(resolver);
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("index.html");
    }


    /**
     * 这个地方要重新注入一下资源文件，不然不会注入资源的，也没有注入requestHandlerMappping,相当于xml配置的
     * <pre>
     * {@code
     * <!--swagger资源配置-->
     * <mvc:resources location="classpath:/META-INF/resources/" mapping="swagger-ui.html"/>
     * <mvc:resources location="classpath:/META-INF/resources/webjars/" mapping="/webjars/**"/>
     * }
     * </pre>
     * 不知道为什么，这也是spring boot的一个缺点（菜鸟觉得的）
     *
     * @param registry ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("/static/");
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    @Bean
    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .groupName("v1")
//                .genericModelSubstitutes(DeferredResult.class)
//                .useDefaultResponseMessages(false)
//                .globalResponseMessage(RequestMethod.GET, customerResponseMessage())
//                .forCodeGeneration(true)
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .paths(PathSelectors.any())
//                .build();


        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
//                .paths(PathSelectors.any())
                .paths(PathSelectors.ant("/gray/**"))
                .build()
                //2 定义了API的根路径
//                .pathMapping("/")
                //3 输出模型定义时的替换，比如遇到所有LocalDate的字段时，输出成String
                .directModelSubstitute(LocalDate.class,
                        String.class)
                //4 遇到对应泛型类型的外围类，直接解析成泛型类型，比如说ResponseEntity<T>，应该直接输出成类型T
                .genericModelSubstitutes(ResponseEntity.class)
                //5 提供了自定义性更强的针对泛型的处理，示例中的代码的意思是将类型DeferredResult>直接解析成类型T
//                .alternateTypeRules(
//                        newRule(typeResolver.resolve(DeferredResult.class,
//                                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
//                                typeResolver.resolve(WildcardType.class)))

                //6 是否使用默认的ResponseMessage， 框架默认定义了一些针对各个HTTP方法时各种不同响应值对应的message
//                .useDefaultResponseMessages(false)
                //7 全局的定义ResponseMessage，示例代码定义GET方法的500错误的消息以及错误模型。注意这里GET方法的所有ResponseMessage都会被这里的定义覆盖
//                .globalResponseMessage(RequestMethod.GET,
//                        newArrayList(new ResponseMessageBuilder()
//                                .code(500)
//                                .message("500 message")
//                                .responseModel(new ModelRef("Error"))
//                                .build()))
                //8 定义API支持的SecurityScheme，指的是认证方式，支持OAuth、APIkey。 P.S. 要让swagger-ui的oauth正常工作，需要定义个SecurityConfiguration的Bean
                .securitySchemes(apiKeys())
                //9 定义具体上下文路径对应的认证方式
                .securityContexts(Arrays.asList(securityContext()));

        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("spring cloud gray server接口列表")
                .description("相关信息请关注：https://github.com/SpringCloud/spring-cloud-gray")
                .termsOfServiceUrl("https://github.com/SpringCloud/spring-cloud-gray")
//                .version("1.0.0")
                .build();
    }


//    @Autowired
//    private TypeResolver typeResolver;

    private List<ApiKey> apiKeys() {
        return Arrays.asList(
//                new ApiKey(AdminInterceptor.AUTH_HEADER, "token", "header"),
//                new ApiKey(CompanyUserInterceptor.AUTH_HEADER, "companyToken", "header")
        );
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/anyPath.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList();
//        return Arrays.asList(
//                new SecurityReference(AdminInterceptor.AUTH_HEADER, authorizationScopes),
//                new SecurityReference(CompanyUserInterceptor.AUTH_HEADER, authorizationScopes));
    }

//    @Bean
//    SecurityConfiguration security() {
//        return new SecurityConfiguration(
//                "test-app-client-id",
//                "test-app-realm",
//                "test-app",
//                "apiKey");
//    }


    /**
     * 自定义返回信息
     *
     * @return 返回消息列表
     */
    private List<ResponseMessage> customerResponseMessage() {

        return Arrays.asList(new ResponseMessageBuilder()//500
                        .code(500)
                        .message("")
                        .responseModel(new ModelRef("Error"))
                        .build(),
                new ResponseMessageBuilder()//401
                        .code(401)
                        .message("")
                        .build());
    }
}
