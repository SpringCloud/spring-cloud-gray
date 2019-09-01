package cn.springcloud.gray.client.config.properties;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CacheProperties {

    private long maximumSize = 500;

    private long expireSeconds = 3600;
}
