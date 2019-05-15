package cn.springcloud.gray.server.dao.mapper;

import java.util.List;

/**
 * Created by saleson on 2017/7/20.
 */
public interface ModelMapper<MO, DO> {

    DO model2do(MO d);

    List<DO> models2dos(Iterable<MO> d);

    MO do2model(DO d);

    List<MO> dos2models(List<DO> d);
}
