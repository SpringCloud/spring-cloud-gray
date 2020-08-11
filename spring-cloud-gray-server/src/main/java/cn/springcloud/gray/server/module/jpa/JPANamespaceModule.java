package cn.springcloud.gray.server.module.jpa;

import cn.springcloud.gray.server.constant.AuthorityConstants;
import cn.springcloud.gray.server.module.NamespaceFinder;
import cn.springcloud.gray.server.module.NamespaceModule;
import cn.springcloud.gray.server.module.domain.Namespace;
import cn.springcloud.gray.server.module.domain.ResourceAuthorityFlag;
import cn.springcloud.gray.server.module.user.AuthorityModule;
import cn.springcloud.gray.server.module.user.domain.UserResourceAuthority;
import cn.springcloud.gray.server.service.NamespaceService;
import cn.springcloud.gray.utils.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-03-22 12:49
 */
public class JPANamespaceModule implements NamespaceModule {

    private NamespaceService namespaceService;
    private NamespaceFinder namespaceFinder;
    private AuthorityModule authorityModule;

    public JPANamespaceModule(
            NamespaceService namespaceService,
            NamespaceFinder namespaceFinder,
            AuthorityModule authorityModule) {
        this.namespaceService = namespaceService;
        this.namespaceFinder = namespaceFinder;
        this.authorityModule = authorityModule;
    }

    @Override
    public Namespace getInfo(String id) {
        return namespaceService.findOneModel(id);
    }

    @Override
    public Page<Namespace> listAll(String userId, Pageable pageable) {
        return namespaceService.findAllByUser(userId, pageable);
    }

    @Override
    public Page<Namespace> listAll(Pageable pageable) {
        return namespaceService.findAllModels(pageable);
    }

    @Override
    public Namespace getUserDefault(String userId) {
        return null;
    }

    @Override
    public Namespace addNamespace(Namespace namespace) {
        Namespace newRecord = namespaceService.saveModel(namespace);

        String userId = namespace.getCreator();

        UserResourceAuthority userResourceAuthority = new UserResourceAuthority();
        userResourceAuthority.setAuthorityFlag(ResourceAuthorityFlag.OWNER);
        userResourceAuthority.setOperator(userId);
        userResourceAuthority.setResource(AuthorityConstants.RESOURCE_NAMESPACE);
        userResourceAuthority.setResourceId(newRecord.getCode());
        userResourceAuthority.setUserId(userId);
        authorityModule.saveUserResourceAuthorityDetail(userResourceAuthority);

        if (StringUtils.isEmpty(getDefaultNamespace(userId))) {
            setDefaultNamespace(userId, newRecord.getCode());
        }
        return newRecord;
    }

    @Override
    public boolean deleteNamespace(String code) {
        Namespace record = namespaceService.findOneModel(code);
        if (Objects.isNull(record) || !namespaceFinder.hasResource(code)) {
            return false;
        }
        record.setDelFlag(true);
        namespaceService.saveModel(record);
        return true;
    }

    @Override
    public boolean setDefaultNamespace(String userId, String nsCode) {
        return namespaceService.setDefaultNamespace(userId, nsCode);
    }

    @Override
    public String getDefaultNamespace(String userId) {
        return namespaceService.getDefaultNamespace(userId);
    }

}
