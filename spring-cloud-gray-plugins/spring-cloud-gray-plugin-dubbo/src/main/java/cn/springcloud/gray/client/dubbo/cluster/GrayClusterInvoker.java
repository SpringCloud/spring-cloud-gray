package cn.springcloud.gray.client.dubbo.cluster;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.client.dubbo.GrayDubboHolder;
import cn.springcloud.gray.client.dubbo.configuration.properties.GrayDubboProperties;
import cn.springcloud.gray.client.dubbo.constants.GrayDubboConstants;
import cn.springcloud.gray.client.dubbo.servernode.InvokerExplainHelper;
import cn.springcloud.gray.commons.GrayRequestHelper;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectPointContext;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectionPoint;
import cn.springcloud.gray.utils.ClassUtils;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.ClusterInvoker;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.model.ConsumerMethodModel;
import org.apache.dubbo.rpc.model.MethodDescriptor;
import org.apache.dubbo.rpc.model.ServiceRepository;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

/**
 * @author saleson
 * @date 2020-09-12 00:02
 */
public class GrayClusterInvoker<T> implements ClusterInvoker<T> {

    private final Directory<T> directory;

    private final Invoker<T> invoker;

    private Set<String> ignoreGrayServiceNames = new HashSet<>();


    public GrayClusterInvoker(Directory<T> directory, Invoker<T> invoker) {
        this.directory = directory;
        this.invoker = invoker;
        initIgnoreGrayServiceNames();
    }

    @Override
    public URL getRegistryUrl() {
        return directory.getUrl();
    }

    @Override
    public Directory<T> getDirectory() {
        return directory;
    }

    @Override
    public Class<T> getInterface() {
        return directory.getInterface();
    }

    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        RoutingConnectionPoint routingConnectionPoint = GrayClientHolder.getRoutingConnectionPoint();
        if (ignoreGrayScale(invocation) || Objects.isNull(routingConnectionPoint)) {
            return this.invoker.invoke(invocation);
        }

        String serviceId = InvokerExplainHelper.getServiceId(invoker);
        GrayRequest grayRequest = createGrayRequest(serviceId, invocation);

        RoutingConnectPointContext connectPointContext = RoutingConnectPointContext.builder()
                .interceptroType(GrayDubboConstants.INTERCEPTRO_TYPE_DUBBO)
                .grayRequest(grayRequest).build();


        return routingConnectionPoint.execute(connectPointContext, () -> invoker.invoke(invocation));
    }


    @Override
    public URL getUrl() {
        return directory.getConsumerUrl();
    }

    @Override
    public boolean isAvailable() {
        return directory.isAvailable();
    }

    @Override
    public void destroy() {
        invoker.destroy();
    }

    private GrayRequest createGrayRequest(String serviceId, Invocation invocation) {
        GrayRequest grayRequest = new GrayRequest();
        grayRequest.setServiceId(serviceId);

        grayRequest.setAttachment(GrayDubboConstants.INVOKE_INVOCATION, invocation);

        String serviceName = invocation.getServiceName();
        String methodName = invocation.getMethodName();
        grayRequest.setAttribute(GrayDubboConstants.DUBBO_INVOCATION_KEY_SERVICE_NAME, serviceName);
        grayRequest.setAttribute(GrayDubboConstants.DUBBO_INVOCATION_KEY_METHOD_NAME, methodName);
        grayRequest.setAttachment(GrayDubboConstants.DUBBO_INVOCATION_KEY_ARGUMENTS, invocation.getArguments());
        grayRequest.setAttachment(GrayDubboConstants.DUBBO_INVOCATION__KEY_METHOD_ARGUMENTS, getMethodArguments(invocation));

        grayRequest.setAttachment(GrayDubboConstants.DUBBO_INVOCATION__KEY_PARAMETER_TYPES, invocation.getParameterTypes());
        if (invocation instanceof RpcInvocation) {
            RpcInvocation rpcInvocation = (RpcInvocation) invocation;
            grayRequest.setAttachment(GrayDubboConstants.DUBBO_INVOCATION__KEY_RETURN_TYPE, rpcInvocation.getReturnType());
            grayRequest.setAttribute(GrayDubboConstants.DUBBO_INVOCATION__KEY_PARAMETER_TYPES_DESC, rpcInvocation.getParameterTypesDesc());
        }

        grayRequest.setUri(URI.create(serviceName + GrayDubboConstants.DUBBO_INVOCATION__KEY_SERVICE_METHOD_SPLIT_JOINT + methodName));
        GrayRequestHelper.setPreviousServerInfoByInstanceLocalInfo(grayRequest);
        return grayRequest;
    }

    protected boolean ignoreGrayScale(Invocation invocation) {
        String serviceName = invocation.getServiceName();
        if (ignoreGrayServiceNames.contains(serviceName)) {
            return true;
        }

        GrayDubboProperties grayDubboProperties = GrayDubboHolder.getGrayDubboProperties();
        if (Objects.isNull(grayDubboProperties)) {
            return false;
        }
        if (grayDubboProperties.getIgnoreGrayServiceNames().contains(serviceName)) {
            return true;
        }
        return false;
    }

    protected void initIgnoreGrayServiceNames() {
        ignoreGrayServiceNames.add(GenericService.class.getName());
    }

    private Map<String, Object> getMethodArguments(Invocation invocation) {
        Object[] arguments = invocation.getArguments();
        Map<String, Object> methodArgs = new HashMap<>(arguments.length);
        Method method = getInvocationMethod(invocation);
        if (Objects.isNull(method)) {
            return methodArgs;
        }
        String[] argNames = ClassUtils.getParameterNames(method);
        if (!Objects.equals(argNames.length, arguments.length)) {
            return methodArgs;
        }
        for (int i = 0; i < argNames.length; i++) {
            methodArgs.put(argNames[i], arguments[i]);
        }
        return methodArgs;
    }

    private Method getInvocationMethod(Invocation invocation) {
        Method method = getMethodByConsumerMethodModel(invocation);
        return Objects.nonNull(method) ? method : getMethodByMethodDescriptor(invocation);
    }


    private Method getMethodByConsumerMethodModel(Invocation invocation) {
        ConsumerMethodModel consumerMethodModel = (ConsumerMethodModel) invocation.getAttributes().get("methodModel");
        if (Objects.isNull(consumerMethodModel)) {
            return null;
        }
        return consumerMethodModel.getMethod();
    }

    private Method getMethodByMethodDescriptor(Invocation invocation) {
        ServiceRepository repository = ApplicationModel.getServiceRepository();
        MethodDescriptor methodDescriptor = repository.lookupMethod(invocation.getServiceName(), invocation.getMethodName());
        if (Objects.isNull(methodDescriptor)) {
            return null;
        }
        return methodDescriptor.getMethod();
    }

    public Set<String> getIgnoreGrayServiceNames() {
        return ignoreGrayServiceNames;
    }
}
