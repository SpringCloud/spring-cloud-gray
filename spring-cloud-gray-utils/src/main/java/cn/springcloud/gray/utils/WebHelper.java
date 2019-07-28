package cn.springcloud.gray.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebHelper {

    private static final Logger LOG = LoggerFactory.getLogger(WebHelper.class);

    public static final String CHARSET_UTF8 = "UTF-8";


    public static void responceJson(HttpServletResponse response, String content) throws IOException {
        response.setCharacterEncoding(CHARSET_UTF8);
        response.setContentType("application/json; charset=" + CHARSET_UTF8);
        String jsonString = content;
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(jsonString.getBytes(CHARSET_UTF8));
    }

    public static void writerResponceContent(HttpServletResponse response, String content) {
        try {
            responceJson(response, content);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
