package cn.springcloud.gray.server.resources.interceptor;

import cn.springcloud.gray.server.module.user.AuthorityModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author saleson
 * @date 2020-03-17 00:15
 */
public class NamespaceInterceptor extends HandlerInterceptorAdapter {

    private String PARAMETER_NAME_NAMESPACE = "namespace";
    private AuthorityModule authorityModule;
    private ObjectMapper objectMapper = new ObjectMapper();

    public NamespaceInterceptor(AuthorityModule authorityModule) {
        this.authorityModule = authorityModule;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
//        String namespace = request.getParameter(PARAMETER_NAME_NAMESPACE);
//        if (StringUtils.isEmpty(namespace)) {
//            return super.preHandle(request, response, handler);
//        }
//        boolean hasAuthority = authorityModule.hasNamespaceAuthority(namespace);
//        if (!hasAuthority) {
//            WebHelper.responceJson(response, objectMapper.writeValueAsString(ApiResHelper.notAuthority()));
//        }
//        return hasAuthority;
    }
}
