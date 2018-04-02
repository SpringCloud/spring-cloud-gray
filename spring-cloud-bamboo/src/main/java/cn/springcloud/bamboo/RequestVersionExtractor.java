package cn.springcloud.bamboo;

public interface RequestVersionExtractor {

    String extractVersion(BambooRequest bambooRequest);


    class Default implements RequestVersionExtractor{
        private static final String VERSION = "version";

        @Override
        public String extractVersion(BambooRequest bambooRequest) {
            return bambooRequest.getParams().getFirst(VERSION);
        }
    }
}
