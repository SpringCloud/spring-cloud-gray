package cn.springcloud.bamboo;

public class ConnectPointContext {

    static final ThreadLocal<ConnectPointContext> contextLocal = new ThreadLocal<>();


    private BambooRequest bambooRequest;
    private Throwable excption;

    private ConnectPointContext(BambooRequest bambooRequest) {
        this.bambooRequest = bambooRequest;
    }

    public BambooRequest getBambooRequest() {
        return bambooRequest;
    }

    void setBambooRequest(BambooRequest bambooRequest) {
        this.bambooRequest = bambooRequest;
    }


    public Throwable getExcption() {
        return excption;
    }

    void setExcption(Throwable excption) {
        this.excption = excption;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private BambooRequest bambooRequest;

        private Builder() {

        }


        public Builder bambooRequest(BambooRequest bambooRequest) {
            this.bambooRequest = bambooRequest;
            return this;
        }

        public ConnectPointContext build() {
            return new ConnectPointContext(bambooRequest);
        }
    }


    public static ConnectPointContext getContextLocal() {
        return contextLocal.get();
    }

}
