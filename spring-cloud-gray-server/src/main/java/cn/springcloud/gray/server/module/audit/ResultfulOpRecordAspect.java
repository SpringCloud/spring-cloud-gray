package cn.springcloud.gray.server.module.audit;

import cn.springcloud.gray.server.module.audit.domain.OperateRecord;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.resources.domain.ApiRes;
import cn.springcloud.gray.utils.WebUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Aspect
public class ResultfulOpRecordAspect {
    private static final Logger log = LoggerFactory.getLogger(ResultfulOpRecordAspect.class);
    private String[] recordMethods = {RequestMethod.POST.name(), RequestMethod.PUT.name(), RequestMethod.DELETE.name()};

    private ObjectMapper objectMapper;
    private UserModule userModule;
    private OperateAuditModule operateAuditModule;

    public ResultfulOpRecordAspect(ObjectMapper objectMapper, UserModule userModule, OperateAuditModule operateAuditModule) {
        this.objectMapper = objectMapper;
        this.userModule = userModule;
        this.operateAuditModule = operateAuditModule;
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping))")
    public void pointcut() {
    }


    @AfterReturning(value = "pointcut()", returning = "result")
    public void doAfter(JoinPoint joinPoint, Object result) {

        RequestMapping requestMapping = getRequestMapping(joinPoint);
        if (!isSholdRecord(requestMapping)) {
            return;
        }


        OperateRecord operateRecord = new OperateRecord();
        operateRecord.setOperateTime(new Date());
        Signature signature = joinPoint.getSignature();
        operateRecord.setHandler(signature.getDeclaringType().getSimpleName() + "#" + signature.getName());
        operateRecord.setOperator(userModule.getCurrentUserId());

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
//        if (requestAttributes != null) {
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//            if (request != null) {
        operateRecord.setUri(request.getRequestURI());
        operateRecord.setHttpMethod(request.getMethod());
        operateRecord.setIp(WebUtils.getIpAddr(request));
        operateRecord.setQueryString(request.getQueryString());
//            }
//        }
        try {
            String HeadlerArgs = desensitizationArgs(request, joinPoint.getArgs());
            operateRecord.setHeadlerArgs(objectMapper.writeValueAsString(HeadlerArgs));
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        if (result instanceof ApiRes) {
            ApiRes apiRes = (ApiRes) result;
            operateRecord.setApiResCode(apiRes.getCode());
            if (StringUtils.equals(apiRes.getCode(), ApiRes.CODE_SUCCESS)) {
                operateRecord.setOperateState(OperateRecord.OPERATE_STATE_SCUUESSED);
            }
        }
        operateAuditModule.recordOperate(operateRecord);
    }

    private Set<String> desensitizationUris = new HashSet<>(
            Arrays.asList("/gray/user/", "/gray/user/login", "/gray/user/resetPassword", "/gray/user/updatePassword"));


    private String[] desensitizationFields = new String[]{"password","oldPassword","newPassword"};

    private String desensitizationArgs(HttpServletRequest request, Object[] args) throws IOException {
        if (desensitizationUris.contains(request.getRequestURI())) {
            List<Map<String, Object>> list = objectMapper.readValue(objectMapper.writeValueAsString(args), new TypeReference<List<Map<String, Object>>>(){});
            if(CollectionUtils.isNotEmpty(list)){
                Map<String, Object> map = list.get(0);
                Object v;
                for (String field : desensitizationFields){
                    v = map.get(field);
                    if(!Objects.isNull(v)){
                        map.put(field, convertDesensitization(v.toString()));
                    }
                }

            }
            return objectMapper.writeValueAsString(list);
        }
        return objectMapper.writeValueAsString(args);
    }


    private String convertDesensitization(String str){
        StringBuilder sb = new StringBuilder();
        for(int i=0, l=str.length(); i<l;i++){
            sb.append('*');
        }
        return sb.toString();
    }


    private boolean isSholdRecord(RequestMapping requestMapping) {
        if (requestMapping == null) {
            return false;
        }
        return isSholdRecord(requestMapping.method()) && sholdFromRequest();
    }

    private RequestMapping getRequestMapping(JoinPoint joinPoint) {
        if (joinPoint.getSignature() instanceof MethodSignature) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            RequestMapping requestMapping = AnnotationUtils.findAnnotation(signature.getMethod(), RequestMapping.class);
            if (requestMapping == null) {
                return AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), RequestMapping.class);
            } else {
                return requestMapping;
            }
        }
        return null;
    }

    private boolean isSholdRecord(RequestMethod[] methods) {
        for (RequestMethod method : methods) {
            if (ArrayUtils.contains(recordMethods, method.name())) {
                return true;
            }
        }
        return false;
    }

    private boolean sholdFromRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (requestAttributes == null) {
            return false;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        if (request == null) {
            return false;
        }
        return ArrayUtils.contains(recordMethods, request.getMethod());
    }

}
