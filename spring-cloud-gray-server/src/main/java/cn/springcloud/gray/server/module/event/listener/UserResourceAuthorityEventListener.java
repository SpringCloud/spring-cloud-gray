package cn.springcloud.gray.server.module.event.listener;

import cn.springcloud.gray.server.constant.AuthorityConstants;
import cn.springcloud.gray.server.module.NamespaceModule;
import cn.springcloud.gray.server.module.user.AuthorityModule;
import cn.springcloud.gray.server.module.user.domain.UserResourceAuthority;
import cn.springcloud.gray.server.module.user.domain.event.UserResourceAuthorityEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;

/**
 * @author saleson
 * @date 2020-08-11 20:18
 */
public class UserResourceAuthorityEventListener implements ApplicationListener<UserResourceAuthorityEvent> {

    private NamespaceModule namespaceModule;
    private AuthorityModule authorityModule;

    public UserResourceAuthorityEventListener(NamespaceModule namespaceModule, AuthorityModule authorityModule) {
        this.namespaceModule = namespaceModule;
        this.authorityModule = authorityModule;
    }

    @Override
    public void onApplicationEvent(UserResourceAuthorityEvent event) {
        UserResourceAuthority userResourceAuthority = event.getUserResourceAutority();
        if (!StringUtils.equals(userResourceAuthority.getResource(), AuthorityConstants.RESOURCE_NAMESPACE)) {
            return;
        }
        switch (event.getDataOPType()) {
            case ADD:
                handleAdd(userResourceAuthority);
                break;
            case DELETE:
                handleDelete(userResourceAuthority);
                break;
        }
    }

    private void handleAdd(UserResourceAuthority userResourceAuthority) {
        String defaultNsCode = namespaceModule.getDefaultNamespace(userResourceAuthority.getUserId());
        if (StringUtils.isNotEmpty(defaultNsCode)) {
            return;
        }
        namespaceModule.setDefaultNamespace(userResourceAuthority.getUserId(), userResourceAuthority.getResourceId());
    }

    private void handleDelete(UserResourceAuthority userResourceAuthority) {
        String defaultNsCode = namespaceModule.getDefaultNamespace(userResourceAuthority.getUserId());
        if (!StringUtils.equals(defaultNsCode, userResourceAuthority.getResourceId())) {
            return;
        }
        String nsCode = authorityModule.firstAuthorityResourceId(userResourceAuthority.getUserId(), AuthorityConstants.RESOURCE_NAMESPACE);
        if (StringUtils.isEmpty(nsCode)) {
            return;
        }
        namespaceModule.setDefaultNamespace(userResourceAuthority.getUserId(), nsCode);
    }
}
