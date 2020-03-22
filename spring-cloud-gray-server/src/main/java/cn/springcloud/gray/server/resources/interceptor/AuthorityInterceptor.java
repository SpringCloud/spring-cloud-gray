package cn.springcloud.gray.server.resources.interceptor;

import cn.springcloud.gray.server.module.domain.OperationAuthrity;
import cn.springcloud.gray.server.module.user.AuthorityModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限拦截
 *
 * @author saleson
 * @date 2020-03-17 00:15
 */
public class AuthorityInterceptor extends HandlerInterceptorAdapter {

    private static final String URI_GRAY_PREFIX = "/gray";
    private AuthorityModule authorityModule;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, OperationAuthrity> methodAuthorityMapping = new HashMap<>();

    public AuthorityInterceptor(AuthorityModule authorityModule) {
        this.authorityModule = authorityModule;
        initMethodAuthorityMapping();
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String resource = getResource(request);
//        String method = request.getMethod();
//        OperationAuthrity operationAuthrity = methodAuthorityMapping.get(method.toLowerCase());
//        if (!Objects.isNull(operationAuthrity) &&
//                !authorityModule.hasAuthorityCurrentUser(resource, operationAuthrity)) {
//            WebHelper.responceJson(response, objectMapper.writeValueAsString(ApiResHelper.notAuthority()));
//            return false;
//        }
        return true;
    }


    protected void initMethodAuthorityMapping() {
        methodAuthorityMapping.put(HttpMethod.POST.name().toLowerCase(), OperationAuthrity.EDIT);
        methodAuthorityMapping.put(HttpMethod.PUT.name().toLowerCase(), OperationAuthrity.EDIT);
        methodAuthorityMapping.put(HttpMethod.PATCH.name().toLowerCase(), OperationAuthrity.EDIT);
        methodAuthorityMapping.put(HttpMethod.GET.name().toLowerCase(), OperationAuthrity.READ);
        methodAuthorityMapping.put(HttpMethod.DELETE.name().toLowerCase(), OperationAuthrity.DELETE);
    }

    protected String getResource(HttpServletRequest request) {
        String uri = request.getRequestURI();
        int cutTimes = 2;
        if (StringUtils.startsWith(uri, URI_GRAY_PREFIX)) {
            cutTimes++;
        }
        String cutstr = cutLeftStr(uri, "/", cutTimes);
        return StringUtils.join(cutstr.substring(1).split("/"), "_");
    }


    private String cutLeftStr(String ori, String flag, int endTimes) {
        int idx = 0;
        for (int i = 0; i < endTimes; i++) {
            idx = ori.indexOf(flag, idx);
        }
        return ori.substring(0, idx);
    }
}
