package cn.springcloud.gray.server.configuration.apidoc;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageableParameterAlternateTypeRuleConvention implements AlternateTypeRuleConvention {

    private TypeResolver resolver;

    public PageableParameterAlternateTypeRuleConvention(TypeResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public List<AlternateTypeRule> rules() {
        return new ArrayList(Arrays.asList(new AlternateTypeRule(resolver.resolve(Pageable.class), resolver.resolve(Page.class))));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }


    @ApiModel
    @Data
    public static class Page {
        @ApiModelProperty(value = "第page页,从0开始计数", example = "0")
        private Integer page = 0;

        @ApiModelProperty(value = "每页数据数量", example = "10")
        private Integer size = 10;

        @ApiModelProperty("按属性排序,格式:属性(,asc|desc)")
        private List<String> sort;
    }
}

