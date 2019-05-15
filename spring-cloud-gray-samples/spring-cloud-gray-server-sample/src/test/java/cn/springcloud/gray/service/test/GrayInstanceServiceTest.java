package cn.springcloud.gray.service.test;


import cn.springcloud.gray.model.GrayStatus;
import cn.springcloud.gray.server.app.GrayServerApplication;
import cn.springcloud.gray.server.module.domain.GrayInstance;
import cn.springcloud.gray.server.module.domain.InstanceStatus;
import cn.springcloud.gray.server.service.GrayInstanceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@ActiveProfiles({"dev"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrayServerApplication.class)
@Slf4j
public class GrayInstanceServiceTest {


    @Autowired
    private GrayInstanceService grayInstanceService;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void test() throws JsonProcessingException {
        List<GrayInstance> grayInstances = grayInstanceService.findAllByStatus(GrayStatus.OPEN, InstanceStatus.UP);
        log.info("{}", objectMapper.writeValueAsString(grayInstances));

    }
}
