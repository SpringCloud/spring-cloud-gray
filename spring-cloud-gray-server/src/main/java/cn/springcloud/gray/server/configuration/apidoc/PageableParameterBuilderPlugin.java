package cn.springcloud.gray.server.configuration.apidoc;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import org.springframework.data.domain.Pageable;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.ResolvedTypes.modelRefFactory;
import static springfox.documentation.spi.schema.contexts.ModelContext.inputParam;

public class PageableParameterBuilderPlugin implements ParameterBuilderPlugin {
    private final TypeNameExtractor nameExtractor;
    private final TypeResolver resolver;

    public PageableParameterBuilderPlugin(TypeNameExtractor nameExtractor, TypeResolver resolver) {
        this.nameExtractor = nameExtractor;
        this.resolver = resolver;
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private Function<ResolvedType, ? extends ModelReference>
    createModelRefFactory(ParameterContext context) {
        ModelContext modelContext = inputParam("",
                context.resolvedMethodParameter().getParameterType(),
                context.getDocumentationType(),
                context.getAlternateTypeProvider(),
                context.getGenericNamingStrategy(),
                context.getIgnorableParameterTypes());
        return modelRefFactory(modelContext, nameExtractor);
    }

    @Override
    public void apply(ParameterContext context) {
        ResolvedMethodParameter parameter = context.resolvedMethodParameter();
        Class<?> type = parameter.getParameterType().getErasedType();

        System.out.println(type);
        if (type != null && Pageable.class.isAssignableFrom(type)) {
            Function<ResolvedType, ? extends ModelReference> factory =
                    createModelRefFactory(context);

            ModelReference intModel = factory.apply(resolver.resolve(Integer.TYPE));
            ModelReference stringModel = factory.apply(resolver.resolve(List.class, String.class));

            List<Parameter> parameters = newArrayList(
                    context.parameterBuilder()
                            .parameterType("query").name("page").modelRef(intModel)
                            .description("请求第几页,默认为0(0=第一页,以此类推)")
                            .build(),
                    context.parameterBuilder()
                            .parameterType("query").name("size").modelRef(intModel)
                            .description("每页的条数")
                            .build(),
                    context.parameterBuilder()
                            .parameterType("query").name("sort").modelRef(stringModel).allowMultiple(true)
                            .description("排序条件的格式为: 属性(,asc|desc). "
                                    + "默认的为升序. "
                                    + "支持多个属性的联合排序.")
                            .build());

            context.getOperationContext().operationBuilder().parameters(parameters);
        }
    }

}