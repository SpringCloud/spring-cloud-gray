package cn.springcloud.gray.server.resources.domain.fo;

import cn.springcloud.gray.model.HandleType;
import cn.springcloud.gray.server.module.domain.Handle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-05-31 01:18
 */
@Data
@ApiModel
public class HandleFO {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("处理名称")
    private String name;

    /**
     * 处理类型: 比如Mock
     * 详情请看{@link HandleType#code()}
     */
    @ApiModelProperty("处理类型: 比如 mock")
    private String type;


    public void fillToHandle(Handle handle) {
        handle.setName(name);
        handle.setType(type);
    }


}
