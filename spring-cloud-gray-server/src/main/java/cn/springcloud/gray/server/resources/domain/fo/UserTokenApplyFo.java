package cn.springcloud.gray.server.resources.domain.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ApiModel("申请oauth2 token的基本信息")
@Data
public class UserTokenApplyFo {

    private static final String[] DEFAULT_USER_ROLES = {"ROLE_USER"};


    @ApiModelProperty("用户名")
    @NotNull
    private String username;
    @ApiModelProperty(value = "用户角色", allowableValues = "ROLE_USER")
    private String[] userRoles = DEFAULT_USER_ROLES;
    @ApiModelProperty("扩展信息")
    private Map<String, String> extensions;


    public UserDetails createUserdetails() {
        List<GrantedAuthority> authorities = new ArrayList<>(getUserRoles().length);
        for (String role : getUserRoles()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return new User(getUsername(), "", authorities);
    }

}
