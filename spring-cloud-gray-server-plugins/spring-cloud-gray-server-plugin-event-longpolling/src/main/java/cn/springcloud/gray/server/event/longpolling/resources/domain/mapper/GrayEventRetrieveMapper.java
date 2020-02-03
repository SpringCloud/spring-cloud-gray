package cn.springcloud.gray.server.event.longpolling.resources.domain.mapper;

import cn.springlcoud.gray.event.GrayEvent;
import cn.springlcoud.gray.event.longpolling.domain.vo.GrayEventRetrieveVO;
import cn.springlcoud.gray.event.longpolling.domain.vo.GrayEventVO;
import cn.springlcoud.gray.event.server.GrayEventRetrieveResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author saleson
 * @date 2020-02-03 21:35
 */
@Component
public class GrayEventRetrieveMapper {


    public GrayEventRetrieveVO toGrayEventRetrieveVO(GrayEventRetrieveResult retrieveResult) {
        GrayEventRetrieveVO vo = new GrayEventRetrieveVO();
        vo.setMaxSortMark(retrieveResult.getMaxSortMark());
        if (CollectionUtils.isEmpty(retrieveResult.getGrayEvents())) {
            return vo;
        }
        List<GrayEventVO> eventVOList = new ArrayList<>(retrieveResult.getGrayEvents().size());
        for (GrayEvent event : retrieveResult.getGrayEvents()) {
            GrayEventVO eventVo = new GrayEventVO();
            eventVo.setEvent(event);
            eventVo.setEventClass(event.getClass().getName());
            eventVOList.add(eventVo);
        }
        return vo;
    }
}
