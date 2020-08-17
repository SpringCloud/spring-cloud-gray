package cn.springcloud.gray.server.event.longpolling.resources.domain.mapper;

import cn.springlcoud.gray.event.GrayEvent;
import cn.springlcoud.gray.event.GrayEventRetrieveResult;
import cn.springlcoud.gray.event.codec.GrayEventEncoder;
import cn.springlcoud.gray.event.longpolling.domain.vo.GrayEventRetrieveVO;
import cn.springlcoud.gray.event.longpolling.domain.vo.GrayEventVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author saleson
 * @date 2020-02-03 21:35
 */
@Component
@Slf4j
public class GrayEventRetrieveMapper {

    @Autowired
    private GrayEventEncoder<String> grayEventEncoder;

    public GrayEventRetrieveVO toGrayEventRetrieveVO(GrayEventRetrieveResult retrieveResult) {
        GrayEventRetrieveVO vo = new GrayEventRetrieveVO();
        vo.setMaxSortMark(retrieveResult.getMaxSortMark());
        if (CollectionUtils.isEmpty(retrieveResult.getGrayEvents())) {
            return vo;
        }
        List<GrayEventVO> eventVOList = new ArrayList<>(retrieveResult.getGrayEvents().size());
        for (GrayEvent event : retrieveResult.getGrayEvents()) {
            GrayEventVO eventVo = new GrayEventVO();
            try {
                eventVo.setEvent(grayEventEncoder.encode(event));
            } catch (IOException e) {
                log.error("encode失败:{}", event, e);
                throw new RuntimeException(e);
            }
            eventVo.setEventClass(event.getClass().getName());
            eventVOList.add(eventVo);
        }
        vo.setGrayEvents(eventVOList);
        return vo;
    }
}
