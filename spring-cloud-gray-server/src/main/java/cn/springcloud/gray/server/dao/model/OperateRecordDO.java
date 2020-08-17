package cn.springcloud.gray.server.dao.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "gray_operate_record", indexes = {@Index(columnList = "operator"), @Index(columnList = "operateTime")})
public class OperateRecordDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 32)
    private String ip;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;
    @Column(length = 256)
    private String uri;
    @Column(length = 16)
    private String httpMethod;
    @Column(length = 512)
    private String queryString;
    @Column(length = 256)
    private String handler;
    @Column(length = 1024)
    private String headlerArgs;
    @Column(length = 16)
    private String apiResCode;
    /**
     * 操作结论, 0:failed, 1: scuuessed
     */
    @Column(length = 2)
    private Integer operateState;
}
