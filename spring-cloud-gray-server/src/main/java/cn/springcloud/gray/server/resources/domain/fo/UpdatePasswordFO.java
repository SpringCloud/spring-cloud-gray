package cn.springcloud.gray.server.resources.domain.fo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class UpdatePasswordFO {
    private String oldPassword;
    private String newPassword;

}
