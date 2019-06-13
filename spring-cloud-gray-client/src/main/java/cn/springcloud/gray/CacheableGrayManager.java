package cn.springcloud.gray;

import cn.springcloud.gray.decision.GrayDecision;

import java.util.List;

public interface CacheableGrayManager extends UpdateableGrayManager {


    Cache<String, List<GrayDecision>> getGrayDecisionCache();

}
