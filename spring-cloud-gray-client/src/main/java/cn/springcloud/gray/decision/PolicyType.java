package cn.springcloud.gray.decision;

public enum PolicyType {

    /**
     * @see cn.springcloud.bamboo.BambooRequest#params
     */
    REQUEST_PARAMETER,
    /**
     * @see cn.springcloud.bamboo.BambooRequest#headers
     */
    REQUEST_HEADER,

    /**
     * @see cn.springcloud.bamboo.BambooRequest#ip
     */
    REQUEST_IP,
    /**
     * @see cn.springcloud.bamboo.BambooRequestContext#params
     */
    CONTEXT_PARAMS
}
