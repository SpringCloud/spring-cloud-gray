package cn.springcloud.gray.server.module.user;

import cn.springcloud.gray.server.constant.AuthorityConstants;
import cn.springcloud.gray.server.module.domain.OperationAuthrity;
import cn.springcloud.gray.server.module.domain.ResourceAuthorityFlag;
import cn.springcloud.gray.server.module.user.domain.AuthorityDetail;
import cn.springcloud.gray.server.module.user.domain.AuthorityQuery;
import cn.springcloud.gray.server.module.user.domain.UserResourceAuthority;
import cn.springcloud.gray.server.module.user.domain.UserResourceAuthorityQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author saleson
 * @date 2020-03-13 00:59
 */
public interface AuthorityModule {

    boolean hasAuthorityCurrentUser(String resource);

    boolean hasAuthorityCurrentUser(String resource, OperationAuthrity operationAuthrity);

    boolean hasAuthorityCurrentUser(String resource, String authority);

    boolean hasAuthorityByUser(String userId, String resource);

    boolean hasAuthorityByUser(String userId, String resource, OperationAuthrity operationAuthrity);

    boolean hasAuthorityByUser(String userId, String resource, String authority);

    boolean hasAuthorityByRole(String role, String resource);

    boolean hasAuthorityByRole(String role, String resource, String authority);

    boolean hasAuthorityByRole(String role, String resource, OperationAuthrity operationAuthrity);

    Page<AuthorityDetail> queryAuthorities(AuthorityQuery query, Pageable pageable);

    AuthorityDetail saveUserResourceAuthorityDetail(AuthorityDetail authorityDetail);

    AuthorityDetail getAuthorityDetail(Long id);

    void updateAuthorityDetailDelFlag(Long id, boolean newDelFlag, String operator);

    boolean hasResourceAuthority(String userId, String resource, String resourceId, ResourceAuthorityFlag authorityFlag);

    boolean hasResourceAuthority(String resource, String resourceId, ResourceAuthorityFlag authorityFlag);

    boolean hasResourceAuthority(String userId, String resource, String resourceId);

    boolean hasResourceAuthority(String resource, String resourceId);

    default boolean hasNamespaceAuthority(String namespace) {
        return hasResourceAuthority(AuthorityConstants.RESOURCE_NAMESPACE, namespace);
    }

    Page<UserResourceAuthority> queryUserResourceAuthority(UserResourceAuthorityQuery query, Pageable pageable);

    UserResourceAuthority saveUserResourceAuthorityDetail(UserResourceAuthority authorityDetail);

    void updateUserResourceAuthorityDelFlag(Long id, boolean newDelFlag, String operator);

    UserResourceAuthority getUserResourceAuthority(Long id);
}
