package cn.springcloud.service.a;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class RestTemplateTest {

    @Test
    public void test() {
        RestTemplate rest = new RestTemplate();
        String url = "http://localhost:20202/gray/instance/{id}/switchStatus?switch=0";
        Map<String, String> params = new HashMap<>();
        params.put("id", "service-a:20104");
        rest.put(url, null, "service-a:20104");
    }
}
