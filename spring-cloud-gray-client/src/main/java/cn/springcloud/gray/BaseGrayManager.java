package cn.springcloud.gray;

import cn.springcloud.gray.client.GrayClientAppContext;
import cn.springcloud.gray.client.GrayClientConfig;
import cn.springcloud.gray.client.GrayOptionalArgs;
import cn.springcloud.gray.core.GrayInstance;
import cn.springcloud.gray.core.GrayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 在AbstractGrayManager基础上进行了扩展，将灰度列表缓存起来，定时从灰度服务端更新灰度列表。
 */
public class BaseGrayManager extends AbstractGrayManager {
    private static final Logger log = LoggerFactory.getLogger(BaseGrayManager.class);
    private Map<String, GrayService> grayServiceMap;
    private Timer updateTimer = new Timer("Gray-UpdateTimer", true);
    private GrayClientConfig clientConfig;

    public BaseGrayManager(GrayOptionalArgs grayOptionalArgs) {
        super(grayOptionalArgs.getInformationClient(), grayOptionalArgs.getDecisionFactory());
        clientConfig = grayOptionalArgs.getGrayClientConfig();
        grayServiceMap = new ConcurrentHashMap<>();
    }


    @Override
    public void openForWork() {
        if (clientConfig.isGrayEnroll()) {
            grayEnroll();
        }
        log.info("拉取灰度列表");
        doUpdate();
        updateTimer.schedule(new UpdateTask(),
                clientConfig.getServiceUpdateIntervalTimerInMs(),
                clientConfig.getServiceUpdateIntervalTimerInMs());
    }

    @Override
    public List<GrayService> listGrayService() {
        if (grayServiceMap == null) {
            List<GrayService> grayServices = super.listGrayService();
            if (grayServices == null) {
                return null;

            }
            updateGrayServices(grayServices);
        }
        return new ArrayList<>(grayServiceMap.values());
    }


    @Override
    public GrayService grayService(String serviceId) {
        if (grayServiceMap == null) {
            return super.grayService(serviceId);
        }
        return grayServiceMap.get(serviceId);
    }

    @Override
    public GrayInstance grayInstance(String serviceId, String instanceId) {
        if (grayServiceMap == null) {
            return super.grayInstance(serviceId, instanceId);
        }
        GrayService grayService = grayService(serviceId);
        if (grayService != null) {
            return grayService.getGrayInstance(instanceId);
        }
        return null;
    }

    @Override
    protected void serviceShutdown() {
        updateTimer.cancel();
    }

    @Override
    public void updateGrayServices(Collection<GrayService> grayServices) {
        if (grayServices == null) {
            return;
        }

        Map<String, GrayService> grayMap = new HashMap<>();
        grayServices.forEach(grayService -> grayMap.put(grayService.getServiceId(), grayService));
        grayServiceMap = new ConcurrentHashMap<>(grayMap);
        checkLocalGray();
    }


    private void doUpdate() {
        try {
            log.debug("更新灰度服务列表...");
            updateGrayServices(client.listGrayService());
        } catch (Exception e) {
            log.error("更新灰度服务列表失败", e);
        }
    }


    private void grayEnroll() {
        Thread t = new Thread(() -> {

            try {
                Thread.sleep(clientConfig.grayEnrollDealyTimeInMs());
            } catch (InterruptedException e) {
            }
            log.info("灰度注册自身实例...");
            InstanceLocalInfo localInfo = GrayClientAppContext.getInstanceLocalInfo();
            try {
                client.addGrayInstance(localInfo.getServiceId(), localInfo.getInstanceId());
                localInfo.setGray(true);
            } catch (Exception e) {
                log.error("自身实例灰度注册失败", e);
            }
        }, "GrayEnroll");
        t.start();
    }

    private void checkLocalGray() {
        InstanceLocalInfo localInfo = GrayClientAppContext.getInstanceLocalInfo();
        GrayService grayService = grayServiceMap.get(localInfo.getServiceId());
        localInfo.setGray(grayService != null && grayService.getGrayInstance(localInfo.getInstanceId()) != null);
    }

    class UpdateTask extends TimerTask {

        @Override
        public void run() {
            doUpdate();
        }
    }


}
