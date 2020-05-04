package cn.springcloud.gray.model;

import lombok.Data;

import java.util.List;

/**
 * @author saleson
 * @date 2020-04-25 18:46
 */
@Data
public class GrayInfos {
    private Long maxSortMark;
    private List<GrayTrackDefinition> trackDefinitions;
    private List<PolicyDefinition> policyDecisions;
    private List<GrayInstance> instances;
}
