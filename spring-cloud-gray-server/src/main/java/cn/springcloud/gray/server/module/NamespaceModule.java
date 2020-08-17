package cn.springcloud.gray.server.module;

import cn.springcloud.gray.server.module.domain.Namespace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author saleson
 * @date 2020-03-16 23:22
 */
public interface NamespaceModule {

    Namespace getInfo(String id);

    Page<Namespace> listAll(String userId, Pageable pageable);

    Page<Namespace> listAll(Pageable pageable);

    Namespace getUserDefault(String userId);

    Namespace addNamespace(Namespace namespace);

    boolean deleteNamespace(String id);

    boolean setDefaultNamespace(String userId, String nsCode);

    String getDefaultNamespace(String userId);


    List<Namespace> listAll(String userId);
}
