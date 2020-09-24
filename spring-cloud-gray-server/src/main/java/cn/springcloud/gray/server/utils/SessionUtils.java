package cn.springcloud.gray.server.utils;

import cn.springcloud.gray.server.GrayServerHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-10 18:33
 */
public class SessionUtils {

    public static String currentUserId() {
        return GrayServerHolder.getOauth2Service().getUserPrincipal();
    }

    public static String currentNamespace() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (Objects.isNull(request)) {
            return null;
        }
        return request.getParameter("ns");
    }
}
