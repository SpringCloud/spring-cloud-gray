package cn.springcloud.gray.service.b.eureka;

import com.netflix.discovery.EurekaEvent;
import com.netflix.discovery.EurekaEventListener;

public class GrayEurekaEventListener implements EurekaEventListener {
    @Override
    public void onEvent(EurekaEvent event) {
        System.out.println(event.toString());
    }
}
