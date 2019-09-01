package cn.springcloud.gray.event;

import cn.springcloud.gray.event.sourcehander.SourceHanderService;
import cn.springcloud.gray.exceptions.EventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraySourceEventListener implements GrayEventListener {

    private static final Logger log = LoggerFactory.getLogger(GraySourceEventListener.class);

    private SourceHanderService sourceHanderService;

    public GraySourceEventListener(SourceHanderService sourceHanderService) {
        this.sourceHanderService = sourceHanderService;
    }

    @Override
    public void onEvent(GrayEventMsg msg) throws EventException {
//        log.debug("");
        log.info("access gray event msg: {}", msg);
        sourceHanderService.handle(msg);
    }
}
