package cn.springcloud.gray.client.config.properties;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CacheProperties {

    private long maximumSize = 1000;

    private long expireSeconds = 60;
}
