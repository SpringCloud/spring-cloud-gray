package cn.springcloud.gray.server.module.user.jpa;

import cn.springcloud.gray.server.module.domain.OperationAuthrity;
import cn.springcloud.gray.server.module.domain.ResourceAuthorityFlag;
import cn.springcloud.gray.server.module.user.AuthorityModule;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.module.user.domain.AuthorityDetail;
import cn.springcloud.gray.server.module.user.domain.AuthorityQuery;
import cn.springcloud.gray.server.module.user.domain.UserResourceAuthority;
import cn.springcloud.gray.server.module.user.domain.UserResourceAuthorityQuery;
import cn.springcloud.gray.server.service.AuthorityService;
import cn.springcloud.gray.server.service.UserResourceAuthorityService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-03-22 09:25
 */
public class JPAAuthorityModule implements AuthorityModule {

    private UserResourceAuthorityService userResourceAuthorityService;
    private AuthorityService authorityService;
    private UserModule userModule;
    private ApplicationEventPublisher eventPublisher;

    public JPAAuthorityModule(
            ApplicationEventPublisher eventPublisher,
            UserResourceAuthorityService userResourceAuthorityService,
            AuthorityService authorityService,
            UserModule userModule) {
        this.userResourceAuthorityService = userResourceAuthorityService;
        this.authorityService = authorityService;
        this.userModule = userModule;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public boolean hasAuthorityCurrentUser(String resource) {
        return hasAuthorityByUser(userModule.getCurrentUserId(), resource);
    }

    @Override
    public boolean hasAuthorityCurrentUser(String resource, OperationAuthrity operationAuthrity) {
        return hasAuthorityByUser(userModule.getCurrentUserId(), resource, operationAuthrity);
    }

    @Override
    public boolean hasAuthorityCurrentUser(String resource, String authority) {
        return hasAuthorityByUser(userModule.getCurrentUserId(), resource, authority);
    }

    @Override
    public boolean hasAuthorityByUser(String userId, String resource) {
//        UserInfo userInfo = userModule.getUserInfo(userId);
//        if (UserInfo.isEanble(userInfo)) {
//            return false;
//        }
//        for (String role : userInfo.getRoles()) {
//            if (hasAuthorityByRole(role, resource)) {
//                return true;
//            }
//        }
//        return false;
        return true;
    }

    @Override
    public boolean hasAuthorityByUser(String userId, String resource, OperationAuthrity operationAuthrity) {
//        UserInfo userInfo = userModule.getUserInfo(userId);
//        if (UserInfo.isEanble(userInfo)) {
//            return false;
//        }
//        for (String role : userInfo.getRoles()) {
//            if (hasAuthorityByRole(role, resource, operationAuthrity)) {
//                return true;
//            }
//        }
//        return false;
        return true;
    }

    @Override
    public boolean hasAuthorityByUser(String userId, String resource, String authority) {
//        UserInfo userInfo = userModule.getUserInfo(userId);
//        if (UserInfo.isEanble(userInfo)) {
//            return false;
//        }
//        for (String role : userInfo.getRoles()) {
//            if (hasAuthorityByRole(role, resource, authority)) {
//                return true;
//            }
//        }
//        return false;
        return true;
    }

    @Override
    public boolean hasAuthorityByRole(String role, String resource) {
//        AuthorityDetail authorityDetail = findAuthorityDetail(role, resource);
//        return !Objects.isNull(authorityDetail)
//                && !authorityDetail.getDelFlag()
//                && ArrayUtils.isNotEmpty(authorityDetail.getAuthorities());
        return true;
    }

    @Override
    public boolean hasAuthorityByRole(String role, String resource, String authority) {
//        AuthorityDetail authorityDetail = findAuthorityDetail(role, resource);
//        return !Objects.isNull(authorityDetail)
//                && !authorityDetail.getDelFlag()
//                && ArrayUtils.contains(authorityDetail.getAuthorities(), authority);
        return true;
    }

    @Override
    public boolean hasAuthorityByRole(String role, String resource, OperationAuthrity operationAuthrity) {
//        AuthorityDetail authorityDetail = findAuthorityDetail(role, resource);
//        return !Objects.isNull(authorityDetail)
//                && !authorityDetail.getDelFlag()
//                && ArrayUtils.contains(authorityDetail.getAuthorities(), operationAuthrity.getCode());
        return true;
    }


    public AuthorityDetail findAuthorityDetail(String role, String resource) {
        return authorityService.findAuthorityDetail(role, resource);
    }

    @Override
    public Page<AuthorityDetail> queryAuthorities(AuthorityQuery query, Pageable pageable) {
        return authorityService.queryAuthorities(query, pageable);
    }

    @Override
    public AuthorityDetail saveUserResourceAuthorityDetail(AuthorityDetail authorityDetail) {
        AuthorityDetail record = findAuthorityDetail(authorityDetail.getRole(), authorityDetail.getResource());
        if (Objects.isNull(record)) {
            record = authorityDetail;
        } else {
            record.reset(authorityDetail);
        }
        setDefault(record);
        return authorityService.saveModel(record);
    }

    @Override
    public AuthorityDetail getAuthorityDetail(Long id) {
        return authorityService.findOneModel(id);
    }

    @Override
    public void updateAuthorityDetailDelFlag(Long id, boolean newDelFlag, String operator) {
        AuthorityDetail record = authorityService.findOneModel(id);
        if (Objects.isNull(record) || Objects.equals(record.getDelFlag(), newDelFlag)) {
            return;
        }
        record.setDelFlag(newDelFlag);
        record.setOperator(operator);
        record.setOperateTime(new Date());
        authorityService.saveModel(record);
    }

    @Override
    public boolean hasResourceAuthority(String userId, String resource, String resourceId, ResourceAuthorityFlag authorityFlag) {
//        UserResourceAuthority record = userResourceAuthorityService.findUserResourceAuthority(userId, resource, resourceId);
//        if (Objects.isNull(record) || record.getDelFlag()) {
//            return false;
//        }
//        return Objects.equals(record.getAuthorityFlag(), authorityFlag);
        return true;
    }

    @Override
    public boolean hasResourceAuthority(String resource, String resourceId, ResourceAuthorityFlag authorityFlag) {
        return hasResourceAuthority(userModule.getCurrentUserId(), resource, resourceId, authorityFlag);
    }

    @Override
    public boolean hasResourceAuthority(String userId, String resource, String resourceId) {
//        UserResourceAuthority record = userResourceAuthorityService.findUserResourceAuthority(userId, resource, resourceId);
//        return !Objects.isNull(record) && !record.getDelFlag();
        return true;
    }

    @Override
    public boolean hasResourceAuthority(String resource, String resourceId) {
        return hasResourceAuthority(userModule.getCurrentUserId(), resource, resourceId);
    }

    @Override
    public Page<UserResourceAuthority> queryUserResourceAuthority(UserResourceAuthorityQuery query, Pageable pageable) {
        return userResourceAuthorityService.queryUserResourceAuthority(query, pageable);
    }

    @Override
    public UserResourceAuthority saveUserResourceAuthorityDetail(UserResourceAuthority userResourceAuthority) {
        UserResourceAuthority record =
                userResourceAuthorityService.findUserResourceAuthority(
                        userResourceAuthority.getUserId(),
                        userResourceAuthority.getResource(),
                        userResourceAuthority.getResourceId());
        if (Objects.isNull(record)) {
            record = userResourceAuthority;
        } else {
            record.reset(userResourceAuthority);
        }
        setDefault(record);
        return userResourceAuthorityService.saveModel(record);
    }

    @Override
    public void updateUserResourceAuthorityDelFlag(Long id, boolean newDelFlag, String operator) {
        UserResourceAuthority record = userResourceAuthorityService.findOneModel(id);
        if (Objects.isNull(record) || Objects.equals(record.getDelFlag(), newDelFlag)) {
            return;
        }
        record.setDelFlag(newDelFlag);
        record.setOperator(operator);
        record.setOperateTime(new Date());
        userResourceAuthorityService.saveModel(record);
    }

    @Override
    public UserResourceAuthority getUserResourceAuthority(Long id) {
        return userResourceAuthorityService.findOneModel(id);
    }

    @Override
    public String firstAuthorityResourceId(String userId, String resourceNamespace) {
        return userResourceAuthorityService.firstAuthorityResourceId(userId, resourceNamespace);
    }

    private void setDefault(UserResourceAuthority record) {
        if (Objects.isNull(record.getDelFlag())) {
            record.setDelFlag(false);
        }
        if (Objects.isNull(record.getOperateTime())) {
            record.setOperateTime(new Date());
        }
        if (Objects.isNull(record.getAuthorityFlag())) {
            record.setAuthorityFlag(ResourceAuthorityFlag.ADMIN);
        }
    }

    private void setDefault(AuthorityDetail record) {
        if (Objects.isNull(record.getDelFlag())) {
            record.setDelFlag(false);
        }
        if (Objects.isNull(record.getOperateTime())) {
            record.setOperateTime(new Date());
        }
    }
}
