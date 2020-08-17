package cn.springcloud.gray.client.config.properties;

import cn.springcloud.gray.IGrayChooseConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author saleson
 * @date 2020-05-28 00:20
 */
@Data
@ConfigurationProperties("gray.choose")
public class GrayChooseProperties implements IGrayChooseConfig {

    private Server server = new Server();

    @Override
    public boolean isChooseServerFairPossible() {
        return server.isFairPossible();
    }


    @Data
    public class Server {
        private boolean fairPossible;
    }
}
