package cn.springcloud.gray.server.dao.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Created by saleson on 2017/7/20.
 */
public interface ModelMapper<MO, DO> {

    DO model2do(MO d);

    List<DO> models2dos(Iterable<MO> d);

    MO do2model(DO d);

    List<MO> dos2models(Iterable<DO> d);

    void do2model(DO d, @MappingTarget MO m);

    void model2do(MO m, @MappingTarget DO d);
}
