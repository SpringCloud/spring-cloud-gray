package cn.springcloud.gray.choose;

import cn.springcloud.gray.ServerListResult;

import java.util.List;

/**
 * @author saleson
 * @date 2020-09-10 23:19
 */
public interface ServerDistinguisher<SERVER> {

    ServerListResult<SERVER> sensitivedistinguish(List<SERVER> servers);

    ServerListResult<SERVER> distinguish(List<SERVER> servers);

}
