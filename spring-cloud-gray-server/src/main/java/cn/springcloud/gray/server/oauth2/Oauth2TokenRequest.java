package cn.springcloud.gray.server.oauth2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class Oauth2TokenRequest {

    private String clientId;
    private Map<String, String> parameters;
    private TokenRequestInfo tokenRequestInfo;
}
