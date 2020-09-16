package cn.springcloud.gray.server.module.jpa;

import cn.springcloud.gray.server.module.NamespaceFinder;
import cn.springcloud.gray.server.module.gray.domain.*;
import cn.springcloud.gray.server.service.*;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author saleson
 * @date 2020-03-22 12:42
 */
public class JPANamespaceFinder implements NamespaceFinder {

    private GrayServiceService grayServiceService;
    private GrayInstanceService grayInstanceService;
    private GrayTrackService grayTrackService;
    private GrayPolicyService grayPolicyService;
    private GrayDecisionService grayDecisionService;

    private Map<GrayModelType, Class<?>> modelTypeClassMappings = new HashMap<>();

    private Map<Class<?>, Function<Object, String>> getFunctions = new HashMap<>();


    public JPANamespaceFinder(
            GrayServiceService grayServiceService,
            GrayInstanceService grayInstanceService,
            GrayTrackService grayTrackService,
            GrayPolicyService grayPolicyService,
            GrayDecisionService grayDecisionService) {
        this.grayServiceService = grayServiceService;
        this.grayInstanceService = grayInstanceService;
        this.grayTrackService = grayTrackService;
        this.grayPolicyService = grayPolicyService;
        this.grayDecisionService = grayDecisionService;
        init();
    }

    public void init() {

        modelTypeClassMappings.put(GrayModelType.SERVICE, GrayService.class);
        modelTypeClassMappings.put(GrayModelType.INSTANCE, GrayInstance.class);
        modelTypeClassMappings.put(GrayModelType.TRACK, GrayTrack.class);
        modelTypeClassMappings.put(GrayModelType.POLICY, GrayPolicy.class);
        modelTypeClassMappings.put(GrayModelType.DECISION, GrayDecision.class);

        getFunctions.put(GrayService.class, this::findByService);
        getFunctions.put(GrayInstance.class, this::findByInstance);
        getFunctions.put(GrayTrack.class, this::findByTrack);
        getFunctions.put(GrayPolicy.class, this::findByPolicy);
        getFunctions.put(GrayDecision.class, this::findByDecision);
    }


    @Override
    public String getNamespaceCode(GrayModelType grayModelType, Object id) {
        Class<?> modelCls = modelTypeClassMappings.get(grayModelType);
        if (modelCls == null) {
            return "";
        }
        return getNamespaceCode(modelCls, id);
    }

    @Override
    public String getNamespaceCode(Class<?> modelCls, Object id) {
        Function<Object, String> function = getFunctions.get(modelCls);
        if (function == null) {
            return "";
        }
        return StringUtils.defaultIfEmpty(function.apply(id), "");
    }

    @Override
    public boolean hasResource(String namespace) {

        return true;
    }

    private String findByService(Object id) {
        GrayService record = grayServiceService.findOneModel((String) id);
        return record == null ? null : record.getNamespace();
    }

    private String findByTrack(Object id) {
        GrayTrack grayTrack = grayTrackService.findOneModel((Long) id);
        return grayTrack == null ? null : findByService(grayTrack.getServiceId());
    }

    private String findByInstance(Object id) {
        GrayInstance grayInstance = grayInstanceService.findOneModel((String) id);
        return grayInstance == null ? null : findByService(grayInstance.getServiceId());
    }

    private String findByPolicy(Object id) {
        GrayPolicy grayPolicy = grayPolicyService.findOneModel((Long) id);
        if (grayPolicy == null) {
            return null;
        }
        return grayPolicy.getNamespace();
    }

    private String findByDecision(Object id) {
        GrayDecision grayDecision = grayDecisionService.findOneModel((Long) id);
        if (grayDecision == null) {
            return null;
        }
        return findByPolicy(grayDecision.getPolicyId());
    }

}
