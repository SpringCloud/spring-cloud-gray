package cn.springcloud.gray.server.resources.domain.mapper;

/**
 * @author saleson
 * @date 2020-02-03 21:23
 */
public interface VOMapper<DTO, VO> {

    VO toVO(DTO dto);
}
