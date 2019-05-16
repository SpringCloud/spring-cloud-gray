package cn.springcloud.gray.request;

import cn.springcloud.gray.model.GrayTrackDefinition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TrackArgs<TRACK extends GrayTrackInfo, REQ> {

    private TRACK trackInfo;
    private REQ request;
    private GrayTrackDefinition trackDefinition;

}
