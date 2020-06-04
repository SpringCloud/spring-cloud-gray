package cn.springlcoud.gray.event;

import cn.springcloud.gray.model.HandleType;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-05-31 15:30
 */
@Data
public class HandleEvent extends GrayEvent {

    private String handleId;
    private String name;

    /**
     * 处理类型: 比如Mock
     * 详情请看{@link HandleType#code()}
     */
    private String type;

    @Override
    public String getSourceId() {
        return handleId;
    }


}
