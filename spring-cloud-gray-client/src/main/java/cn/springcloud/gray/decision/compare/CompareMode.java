package cn.springcloud.gray.decision.compare;

public enum CompareMode {

    EQUAL,
    /**
     * 小于
     */
    LT,
    /**
     * 小于或等于
     */
    LTE,
    /**
     * 大于
     */
    GT,
    /**
     * 大于或等于
     */
    GTE,

    UNEQUAL,
    /**
     * 全部包含
     */
    CONTAINS_ALL,

    /**
     * 全部不包含
     */
    NOT_CONTAINS_ALL,
    /**
     * 至少包含其中一个
     */
    CONTAINS_ANY,

    /**
     * 至少不包含其中一个
     */
    NOT_CONTAINS_ANY
}
