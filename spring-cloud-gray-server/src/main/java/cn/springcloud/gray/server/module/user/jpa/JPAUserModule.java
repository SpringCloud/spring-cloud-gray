package cn.springcloud.gray.server.module.user.jpa;

import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.module.user.domain.UserInfo;
import cn.springcloud.gray.server.module.user.domain.UserQuery;
import cn.springcloud.gray.server.oauth2.Oauth2Service;
import cn.springcloud.gray.server.service.UserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Objects;

public class JPAUserModule implements UserModule {

    private Oauth2Service oauth2Service;
    private UserService userService;

    public JPAUserModule(UserService userService, Oauth2Service oauth2Service) {
        this.oauth2Service = oauth2Service;
        this.userService = userService;
    }

    @Override
    public UserInfo login(String account, String password) {
        return userService.login(account, password);
    }

    @Override
    public UserInfo register(UserInfo userInfo, String password) {
        return userService.register(userInfo, password);
    }

    @Override
    public void disableUser(String userId) {
        userService.updateUserStatus(userId, UserInfo.STATUS_DISABLED);
    }

    @Override
    public void enableUser(String userId) {
        userService.updateUserStatus(userId, UserInfo.STATUS_ENABLED);
    }

    @Override
    public void resetPassword(String userId, String password) {
        userService.resetPassword(userId, password);
    }

    @Override
    public boolean resetPassword(String userId, String oldPassword, String newPassword) {
        return userService.resetPassword(userId, oldPassword, newPassword);
    }

    @Override
    public UserInfo getUserInfo(String userId) {
        return userService.findOneModel(userId);
    }

    @Override
    public UserInfo getCurrentUserInfo() {
        return getUserInfo(getCurrentUserId());
    }

    @Override
    public Page<UserInfo> query(UserQuery userQuery, Pageable pageable) {
        return userService.query(userQuery, pageable);
    }

    @Override
    public String getCurrentUserId() {
        return oauth2Service.getUserPrincipal();
    }

    @Override
    public UserInfo updateUserInfo(UserInfo userInfo) {
        return userService.updateUserInfo(userInfo);
    }

    @Override
    public boolean isAdmin() {
        return isAdmin(getCurrentUserId());
    }

    @Override
    public boolean isAdmin(String userId) {
        return hasRole(userId, UserInfo.ROLE_ADMIN);
    }

    @Override
    public boolean hasRole(String userId) {
        return hasRole(getCurrentUserId());
    }

    @Override
    public boolean hasRole(String userId, String role) {
        UserInfo userInfo = getUserInfo(userId);
        return !Objects.isNull(userInfo) && ArrayUtils.contains(userInfo.getRoles(), role);
    }
}
