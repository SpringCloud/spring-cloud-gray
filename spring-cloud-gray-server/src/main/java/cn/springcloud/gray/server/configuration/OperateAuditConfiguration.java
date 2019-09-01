package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.server.module.audit.OperateAuditModule;
import cn.springcloud.gray.server.module.audit.ResultfulOpRecordAspect;
import cn.springcloud.gray.server.module.user.UserModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OperateAuditConfiguration {

    @Bean
    @ConditionalOnProperty(value = "gray.server.operate.audit.enable", matchIfMissing = true)
    public ResultfulOpRecordAspect resultfulOpRecordAspect(
            @Autowired(required = false) ObjectMapper objectMapper,
            UserModule userModule,
            OperateAuditModule operateAuditModule){
        if(objectMapper==null){
            objectMapper = new ObjectMapper();
        }
        return new ResultfulOpRecordAspect(objectMapper, userModule, operateAuditModule);
    }
}
