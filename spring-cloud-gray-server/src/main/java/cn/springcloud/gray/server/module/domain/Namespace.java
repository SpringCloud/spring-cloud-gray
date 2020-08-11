package cn.springcloud.gray.server.module.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author saleson
 * @date 2020-03-16 23:25
 */
@Data
public class Namespace {


    private String code;

    private String name;

    private Boolean delFlag;

    private Date createTime;

    private String creator;
}
