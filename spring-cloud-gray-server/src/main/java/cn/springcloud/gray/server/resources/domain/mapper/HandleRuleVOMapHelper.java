package cn.springcloud.gray.server.resources.domain.mapper;

import cn.springcloud.gray.server.module.HandleModule;
import cn.springcloud.gray.server.module.domain.Handle;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-31 00:53
 */
@Component
public class HandleRuleVOMapHelper {

    @Autowired
    protected HandleModule handleModule;


    public String parseHandleOptionAlias(String handleOption) {
        if (StringUtils.isEmpty(handleOption)) {
            return null;
        }
        Handle handle = handleModule.getHandle(Long.valueOf(handleOption));
        return Objects.nonNull(handle) ? handle.getName() : null;
    }

}
