package cn.springcloud.gray.handle;

import cn.springcloud.gray.DataSet;
import lombok.Data;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-24 22:29
 */
@Data
public class HandleRuleInfo implements Comparable<HandleRuleInfo> {

    private final String id;
    private String type;
    private final DataSet<String> routePolicies = new DataSet<>();

    private String handleInfo;
    private int order;


    public HandleRuleInfo(String id) {
        this.id = id;
    }


    @Override
    public int compareTo(HandleRuleInfo o) {
        if (Objects.isNull(o)) {
            return -1;
        }
        return Integer.compare(this.getOrder(), o.getOrder());
    }

}
