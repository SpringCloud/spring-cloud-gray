package cn.springcloud.gray.server.dao.mapper;

import cn.springcloud.gray.server.dao.model.NamespaceDO;
import cn.springcloud.gray.server.module.domain.Namespace;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface NamespaceMapper extends ModelMapper<Namespace, NamespaceDO> {

}
