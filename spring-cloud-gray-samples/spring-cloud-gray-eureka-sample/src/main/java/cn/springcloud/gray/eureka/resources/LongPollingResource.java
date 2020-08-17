package cn.springcloud.gray.eureka.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author saleson
 * @date 2020-01-17 23:22
 */
@RestController
@RequestMapping("/test")
public class LongPollingResource {

    private Map<String, DeferredResult<Map<String, String>>> resultMap = new ConcurrentHashMap<>();

    @RequestMapping("/longPolling")
    public DeferredResult<Map<String, String>> longPolling(@RequestParam("id") String id) {
        DeferredResult result = new DeferredResult(60000l);
        result.onTimeout(() -> {
            Map<String, String> map = new HashMap<>();
            map.put("oriId", id);
            map.put("status", "timeout");
            result.setResult(map);
        });
        result.onCompletion(() -> {
            System.out.println(id + " is done.");
            resultMap.remove(id);
        });
        resultMap.put(id, result);
        return result;
    }


    @RequestMapping("/get")
    public Map<String, String> get() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("value", "value");
        return map;
    }

    @RequestMapping("/push")
    public String push(@RequestParam("id") String id, @RequestParam("value") String value) {
        DeferredResult<Map<String, String>> result = resultMap.get(id);
        if (Objects.isNull(result)) {
            return "not found DeferredResult";
        }
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("value", value);
        result.setResult(map);
        return "success";
    }
}
