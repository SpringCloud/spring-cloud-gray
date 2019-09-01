package cn.springcloud.gray.server.resources.domain.fo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class ResetPasswordFO {
    private String userId;
    private String password;
}
