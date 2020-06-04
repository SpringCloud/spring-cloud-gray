package cn.springcloud.gray.server.module.domain.query;

import cn.springcloud.gray.model.HandleType;
import cn.springcloud.gray.server.module.domain.DelFlag;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author saleson
 * @date 2020-05-30 22:58
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class HandleQuery {
    @ApiModelProperty("处理名称")
    private String name;

    @ApiModelProperty("命名空间")
    private String namespace;
    /**
     * 处理类型: 比如Mock
     * 详情请看{@link HandleType#code()}
     */
    @ApiModelProperty("处理类型: 比如 mock")
    private String type;

    @ApiModelProperty("是否删除")
    private DelFlag delFlag = DelFlag.UNDELETE;
}
