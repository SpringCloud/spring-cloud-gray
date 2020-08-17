package cn.springcloud.gray.choose;

import cn.springcloud.gray.ServerListResult;
import cn.springcloud.gray.servernode.ServerSpec;

import java.util.List;

/**
 * 灰度服务器分拣器
 *
 * @author saleson
 * @date 2020-05-08 20:07
 */
public interface GrayServerSorter<SERVER> {


    /**
     * 区分灰度实例和正常实例
     *
     * @param servers
     * @return 返回区分后的结果，如果关闭灰度开关，将会返回null
     */
    ServerListResult<SERVER> distinguishServerList(List<SERVER> servers);

    /**
     * 区分灰度实例和正常实例
     *
     * @param serverSpecs
     * @return 返回区分后的结果，如果关闭灰度开关，将会返回null
     */
    ServerListResult<ServerSpec<SERVER>> distinguishServerSpecList(List<ServerSpec<SERVER>> serverSpecs);


    /**
     * 区分灰度实例和正常实例,并比对灰度实例的灰度决策
     *
     * @param servers
     * @return 返回区分及比对后的结果，如果关闭灰度开关，将会返回null
     */
    ServerListResult<SERVER> distinguishAndMatchGrayServerList(List<SERVER> servers);

    /**
     * 区分灰度实例和正常实例,并比对灰度实例的灰度决策
     *
     * @param serverSpecs
     * @return 返回区分及比对后的结果，如果关闭灰度开关，将会返回null
     */
    ServerListResult<ServerSpec<SERVER>> distinguishAndMatchGrayServerSpecList(List<ServerSpec<SERVER>> serverSpecs);

}
