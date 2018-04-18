package cn.springcloud.bamboo;


/**
 * 这个接口负责获取请求需要访问的目标接口的版本。
 * 比如有些接口版本是放在路径上，如:/v1/api/test/get。也有放在uri参数中:/api/test/get?v=1。
 * 也有可能放到header中，所以在bamboo抽象出来一个接口， 具体的实现由开发者根据业务去实现。
 */
public interface RequestVersionExtractor {

    String extractVersion(BambooRequest bambooRequest);


    /**
     * 默认从querystring中获取：/api/test?version=1
     */
    class Default implements RequestVersionExtractor {
        private static final String VERSION = "version";

        @Override
        public String extractVersion(BambooRequest bambooRequest) {
            return bambooRequest.getParams().getFirst(VERSION);
        }
    }
}
