package cn.springcloud.gray.service.b.service;

import cn.springcloud.gray.service.b.feign.TestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.Future;

@Service
public class TestService {
    @Autowired
    private TestClient testClient;

    @Async("taskExecutor")
    public Future<Map> get(String version) {
        return AsyncResult.forValue(testClient.testGet(version));
    }
}
