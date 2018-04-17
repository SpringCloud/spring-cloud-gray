package cn.springcloud.gray.core;

/**
 * 灰度决策的工厂类
 */
public interface GrayDecisionFactory {


    GrayDecision getDecision(GrayPolicy grayPolicy);
}
