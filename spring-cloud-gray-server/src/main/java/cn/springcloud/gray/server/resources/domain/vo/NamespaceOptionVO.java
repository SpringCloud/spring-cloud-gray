package cn.springcloud.gray.server.resources.domain.vo;

import cn.springcloud.gray.server.module.domain.Namespace;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-03-16 23:25
 */
@Data
public class NamespaceOptionVO {

    private String code;

    private String name;


    public static NamespaceOptionVO of(Namespace namespace) {
        NamespaceOptionVO vo = new NamespaceOptionVO();
        vo.setCode(namespace.getCode());
        vo.setName(namespace.getName());
        return vo;
    }

    public static List<NamespaceOptionVO> of(List<Namespace> namespaces) {
        return namespaces.stream().map(NamespaceOptionVO::of).collect(Collectors.toList());
    }

}
