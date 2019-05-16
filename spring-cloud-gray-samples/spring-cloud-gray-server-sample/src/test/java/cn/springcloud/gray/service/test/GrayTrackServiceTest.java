package cn.springcloud.gray.service.test;

import cn.springcloud.gray.server.app.GrayServerApplication;
import cn.springcloud.gray.server.module.domain.GrayTrack;
import cn.springcloud.gray.server.service.GrayTrackService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles({"dev"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrayServerApplication.class)
@Slf4j
public class GrayTrackServiceTest {

    @Autowired
    private GrayTrackService grayTrackService;

    @Test
    public void test1() {
        GrayTrack grayTrack = GrayTrack.builder()
                .id(1L)
                .serviceId("a")
                .instanceId("")
                .name("test")
                .infos("infos")
                .build();

        grayTrackService.saveModel(grayTrack);


        grayTrackService.saveModel(GrayTrack.builder()
                .serviceId("a")
                .name("test2")
                .infos("infos2")
                .build());

        grayTrackService.saveModel(GrayTrack.builder()
                .serviceId("a")
                .instanceId("a-1")
                .name("test2")
                .infos("infos2")
                .build());

        GrayTrack grayTrack1 = grayTrackService.findOneModel(1L);
        log.info(grayTrack1.toString());

    }
}
