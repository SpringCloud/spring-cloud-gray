package cn.springcloud.gray.server.module.jpa;

import cn.springcloud.gray.server.module.NamespaceFinder;
import cn.springcloud.gray.server.module.NamespaceModule;
import cn.springcloud.gray.server.module.domain.Namespace;
import cn.springcloud.gray.server.service.NamespaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-03-22 12:49
 */
public class JPANamespaceModule implements NamespaceModule {

    private NamespaceService namespaceService;
    private NamespaceFinder namespaceFinder;

    public JPANamespaceModule(NamespaceService namespaceService, NamespaceFinder namespaceFinder) {
        this.namespaceService = namespaceService;
        this.namespaceFinder = namespaceFinder;
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
        return namespaceService.saveModel(namespace);
    }

    @Override
    public boolean deleteNamespace(String code) {
        if (!namespaceFinder.hasResource(code)) {
            return false;
        }
        namespaceService.delete(code);
        return true;
    }

    private void setDefault(Namespace namespace) {
        if (Objects.isNull(namespace.getCreateTime())) {
            namespace.setCreateTime(new Date());
        }
        if (Objects.isNull(namespace.getDelFlag())) {
            namespace.setDelFlag(false);
        }
    }
}
