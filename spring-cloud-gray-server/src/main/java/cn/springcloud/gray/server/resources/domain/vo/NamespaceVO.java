package cn.springcloud.gray.server.resources.domain.vo;

import cn.springcloud.gray.server.module.domain.Namespace;
import lombok.Data;

import java.util.Date;

/**
 * @author saleson
 * @date 2020-03-16 23:25
 */
@Data
public class NamespaceVO {

    private String code;

    private String name;

    private Boolean delFlag;

    private Date createTime;

    private String creator;

    private boolean isDefault;


    public static NamespaceVO of(Namespace namespace) {
        NamespaceVO vo = new NamespaceVO();
        vo.setCode(namespace.getCode());
        vo.setName(namespace.getName());
        vo.setCreateTime(namespace.getCreateTime());
        vo.setDelFlag(namespace.getDelFlag());
        vo.setCreator(namespace.getCreator());
        return vo;
    }

}
