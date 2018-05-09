package cn.springcloud.bamboo.autoconfig.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("multiversion")
public class BambooProperties {

    private BambooRequest bambooRequest = new BambooRequest();

    public BambooRequest getBambooRequest() {
        return bambooRequest;
    }

    public void setBambooRequest(BambooRequest bambooRequest) {
        this.bambooRequest = bambooRequest;
    }

    public static class BambooRequest {

        private boolean loadBody = false;

        /**
         * 是否读取并加载请求的body数据
         *
         * @return
         */
        public boolean isLoadBody() {
            return loadBody;
        }

        public void setLoadBody(boolean loadBody) {
            this.loadBody = loadBody;
        }
    }

}
