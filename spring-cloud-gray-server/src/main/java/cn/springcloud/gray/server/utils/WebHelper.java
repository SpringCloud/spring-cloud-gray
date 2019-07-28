package cn.springcloud.gray.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebHelper {


    private static final ObjectMapper objectMapper = new ObjectMapper();

    private WebHelper(){}


    public static void response(HttpServletResponse response, Object result) throws IOException {
        cn.springcloud.gray.utils.WebHelper.responceJson(response, objectMapper.writeValueAsString(result));
    }


}
