package cn.springcloud.gray.event;

import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-02-01 16:05
 */
@ToString
public class GrayEventRetrieveResult {

    private Long maxSortMark;

    private List<GrayEvent> grayEvents = new ArrayList<>();


    public GrayEventRetrieveResult(List<GrayEvent> grayEvents) {
        if (CollectionUtils.isNotEmpty(grayEvents)) {
            this.grayEvents = grayEvents.stream()
                    .sorted(Comparator.comparing(GrayEvent::getSortMark).reversed()).collect(Collectors.toList());
        }
        retrieveMaxSortMark();
    }

    private void retrieveMaxSortMark() {
        for (int i = grayEvents.size(); i > 0; i--) {
            GrayEvent grayEvent = grayEvents.get(i - 1);
            if (Objects.isNull(grayEvent) || Objects.isNull(grayEvent.getSortMark())) {
                continue;
            }
            maxSortMark = grayEvent.getSortMark();
        }
    }


    public List<GrayEvent> getGrayEvents() {
        return grayEvents;
    }

    public Long getMaxSortMark() {
        return maxSortMark;
    }

    public void setMaxSortMark(Long maxSortMark) {
        this.maxSortMark = maxSortMark;
    }

    public boolean hasResult() {
        return CollectionUtils.isNotEmpty(grayEvents);
    }

}
