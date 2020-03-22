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

    private Map<GrayModelType, Function<Object, String>> getFunctions = new HashMap<>();


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
        getFunctions.put(GrayModelType.TRACK, this::findByTrack);
        getFunctions.put(GrayModelType.INSTANCE, this::findByInstance);
        getFunctions.put(GrayModelType.POLICY, this::findByPolicy);
        getFunctions.put(GrayModelType.DECISION, this::findByDecision);
    }


    @Override
    public String getNamespaceCode(GrayModelType grayModelType, Object id) {
        Function<Object, String> function = getFunctions.get(grayModelType);
        if (function == null) {
            return "";
        }
        return StringUtils.defaultIfEmpty(function.apply(id), "");
    }

    @Override
    public boolean hasResource(String namespace) {

        return false;
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
