package cn.springcloud.gray.service.test;

import cn.springcloud.gray.server.configuration.properties.GrayServerProperties;
import cn.springcloud.gray.server.module.GrayInstanceRecordEvictor;
import cn.springcloud.gray.server.module.domain.GrayInstance;
import cn.springcloud.gray.server.service.GrayInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

//@ActiveProfiles({"dev"})
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = GrayServerApplication.class)
@Slf4j
public class GrayInstanceRecordEvictionTest {

    @Autowired
    private GrayServerProperties grayServerProperties;
    @Autowired
    private GrayInstanceRecordEvictor grayInstanceRecordEvictor;
    @Autowired
    private GrayInstanceService grayInstanceService;

    //    @Test
    public void testEvictGrayInstance() {
        GrayServerProperties.InstanceRecordEvictProperties evictProperties =
                grayServerProperties.getInstance().getEviction();
        List<GrayInstance> befores = grayInstanceService.findAllByEvictableRecords(
                evictProperties.getLastUpdateDateExpireDays(), evictProperties.getEvictionInstanceStatus());

        if (befores.size() > 0) {
            grayInstanceRecordEvictor.evict();
            List<GrayInstance> afters = grayInstanceService.findAllByEvictableRecords(
                    evictProperties.getLastUpdateDateExpireDays(), evictProperties.getEvictionInstanceStatus());
            log.info("before:{}, after:{}", befores.size(), afters.size());
            Assert.assertTrue("清理失败的灰度服务实例失败", afters.size() == 0);
        }
    }
}
