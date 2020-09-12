package cn.springcloud.gray.server.plugin.dubbo.discovery;

import cn.springcloud.gray.model.InstanceInfo;
import cn.springcloud.gray.server.discovery.InstanceInfoAnalyser;
import cn.springcloud.gray.utils.StringUtils;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author saleson
 * @date 2020-09-09 10:36
 */

public class DubboNacosInstanceInfoAnalyser implements InstanceInfoAnalyser<Instance> {

    private static final String protocolsPrefix = "dubbo.protocols";

    private static final Pattern protocolsPattern = Pattern.compile("^dubbo\\.protocols\\.(.+)\\.port$");

    @Override
    public void analyse(Instance instance, InstanceInfo instanceInfo) {
        String[] aliases = getAliases(instance.getIp(), instance.getMetadata());
        instanceInfo.setAliases(aliases);
    }

    private String[] getAliases(String host, Map<String, String> metadata) {
        return metadata.entrySet()
                .stream()
                .map(entry -> getAlias(host, entry.getKey(), entry.getValue()))
                .filter(Objects::nonNull)
                .toArray(String[]::new);
    }


    private String getAlias(String host, String name, String value) {
        if (!isDubboProtocol(name)) {
            return null;
        }
        String protocel = getDubboProtocol(name);
        if (StringUtils.isEmpty(protocel)) {
            return null;
        }
        return protocel + ":" + host + ":" + value;
    }

    private boolean isDubboProtocol(String name) {
        return name.startsWith(protocolsPrefix);
    }

    private String getDubboProtocol(String name) {
        Matcher matcher = protocolsPattern.matcher(name);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group(0);
    }

}
