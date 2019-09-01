package cn.springcloud.gray.server.resources.domain.fo;

import cn.springcloud.gray.server.module.user.domain.UserInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class UserRegisterFO {
    private String name;
    private String account;
    private String password;
    private String[] roles;
    private int status = UserInfo.STATUS_ENABLED;



    public UserInfo toUserInfo(){
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(this.getAccount());
        userInfo.setName(this.getName());
        userInfo.setRoles(this.getRoles());
        userInfo.setStatus(this.getStatus());
        return userInfo;
    }
}
