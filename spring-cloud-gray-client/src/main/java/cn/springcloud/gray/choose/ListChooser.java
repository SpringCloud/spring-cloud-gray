package cn.springcloud.gray.choose;

import java.util.List;

public interface ListChooser<SERVER> {

    default SERVER choose(ChooseGroup chooseGroup, List<SERVER> servers) {
        return choose(chooseGroup.name(), servers);
    }

    /**
     * @param group   {@link ChooseGroup#name()}
     * @param servers
     * @return
     */
    SERVER choose(String group, List<SERVER> servers);
}
