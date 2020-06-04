package cn.springcloud.gray.server.resources.domain.mapper;

import java.util.Collection;
import java.util.List;

/**
 * @author saleson
 * @date 2020-02-03 21:23
 */
public interface VOMapper<DTO, VO> {

    List<VO> toVOs(Collection<DTO> dtos);

    VO toVO(DTO dto);

}
