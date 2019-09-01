package cn.springcloud.gray.server.module.user;

import cn.springcloud.gray.server.module.user.domain.UserInfo;
import cn.springcloud.gray.server.module.user.domain.UserQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserModule {

    UserInfo login(String account, String password);

    UserInfo register(UserInfo userInfo, String password);

    void disableUser(String userId);

    void enableUser(String userId);

    void resetPassword(String userId, String password);

    boolean resetPassword(String userId, String oldPassword, String newPassword);

    UserInfo getUserInfo(String userId);

    UserInfo getCurrentUserInfo();

    Page<UserInfo> query(UserQuery userQuery, Pageable pageable);

    String getCurrentUserId();

    UserInfo updateUserInfo(UserInfo userInfo);
}
