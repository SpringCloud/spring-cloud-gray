package cn.springcloud.gray.client.plugin.ribbon.nacos;

import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.LazyInstanceLocalInfoObtainer;
import cn.springcloud.gray.utils.SpringApplicationContextUtils;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author saleson
 * @date 2020-09-18 11:25
 */
@Slf4j
public class NacosInstanceLocalInfoObtainer extends LazyInstanceLocalInfoObtainer {


    @Override
    public InstanceLocalInfo getInstanceLocalInfo() {
        InstanceLocalInfo instanceLocalInfo = super.getInstanceLocalInfo();
        if (StringUtils.isEmpty(instanceLocalInfo.getInstanceId())) {
            instanceLocalInfo.setInstanceId(getLocalInstanceId());
        }
        return instanceLocalInfo;
    }

    @Override
    protected String getLocalInstanceId() {
        NacosDiscoveryProperties nacosDiscoveryProperties = SpringApplicationContextUtils.getBean(applicationContext, "nacosDiscoveryProperties", NacosDiscoveryProperties.class);
        if (Objects.isNull(nacosDiscoveryProperties)) {
            return "";
        }
        try {
            List<Instance> instances = nacosDiscoveryProperties.
                    namingServiceInstance().getAllInstances(nacosDiscoveryProperties.getService(), nacosDiscoveryProperties.getGroup(), Arrays.asList(nacosDiscoveryProperties.getClusterName()));

            Optional<Instance> instanceOpt = instances.stream().filter(instance -> {
                return StringUtils.equals(instance.getIp(), nacosDiscoveryProperties.getIp()) && Objects.equals(nacosDiscoveryProperties.getPort(), instance.getPort());
            }).findFirst();
            if (!instanceOpt.isPresent()) {
                return "";
            }
            return instanceOpt.get().getInstanceId();
        } catch (NacosException e) {
            log.error("获取nacos instanceId失败 ", e);
        }
        return null;
    }
}
