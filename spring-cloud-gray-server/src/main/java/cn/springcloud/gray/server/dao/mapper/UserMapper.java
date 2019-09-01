package cn.springcloud.gray.server.dao.mapper;

import cn.springcloud.gray.server.dao.model.UserDO;
import cn.springcloud.gray.server.module.user.domain.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UserMapper extends ModelMapper<UserInfo, UserDO> {


    @Mapping(target = "roles", expression="java(ary2str(d.getRoles()))")
    UserDO model2do(UserInfo d);


    @Mapping(target = "roles", expression="java(str2ary(d.getRoles()))")
    UserInfo do2model(UserDO d);


    @Mapping(target = "roles", expression="java(str2ary(d.getRoles()))")
    void do2model(UserDO d, @MappingTarget UserInfo m);

    @Mapping(target = "roles", expression="java(ary2str(m.getRoles()))")
    void model2do(UserInfo m, @MappingTarget UserDO d);

    default String[] str2ary(String str){
        return StringUtils.split(str, ',');
    }

    default String ary2str(String[] ary){
        return StringUtils.join(ary, ',');
    }
}
