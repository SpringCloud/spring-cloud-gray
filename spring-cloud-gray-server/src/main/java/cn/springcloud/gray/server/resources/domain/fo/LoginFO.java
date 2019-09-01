package cn.springcloud.gray.server.resources.domain.fo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginFO {
    private String username;
    private String password;
}
