package cn.springcloud.gray.server.app2.web.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class UserInfoVO {
    private List<String> roles = Arrays.asList("admin");
    private String introduction = "I am a super administrator";
    private String avatar = "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif";
    private String name = "Super Admin";
    private String account;
}
