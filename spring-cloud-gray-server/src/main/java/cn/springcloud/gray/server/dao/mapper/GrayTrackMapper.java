package cn.springcloud.gray.server.dao.mapper;

import cn.springcloud.gray.server.dao.model.GrayTrackDO;
import cn.springcloud.gray.server.module.domain.GrayTrack;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface GrayTrackMapper extends ModelMapper<GrayTrack, GrayTrackDO> {

}
