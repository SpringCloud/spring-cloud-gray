package cn.springcloud.gray.server.event.triggering.converter;

import cn.springcloud.gray.model.GrayInstanceAlias;
import cn.springcloud.gray.event.GrayInstanceAliasEvent;
import cn.springcloud.gray.event.server.AbstrctEventConverter;

/**
 * @author saleson
 * @date 2020-09-09 00:20
 */
public class GrayInstanceAliasEventConverter extends AbstrctEventConverter<GrayInstanceAlias, GrayInstanceAliasEvent> {

    @Override
    protected GrayInstanceAliasEvent convertDeleteData(GrayInstanceAlias instanceAlias) {
        return null;
    }

    @Override
    protected GrayInstanceAliasEvent convertModifyData(GrayInstanceAlias instanceAlias) {
        return new GrayInstanceAliasEvent(instanceAlias);
    }
}
