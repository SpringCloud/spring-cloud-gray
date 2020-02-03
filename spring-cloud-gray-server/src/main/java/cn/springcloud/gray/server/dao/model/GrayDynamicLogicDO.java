package cn.springcloud.gray.server.dao.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author saleson
 * @date 2020-01-31 21:43
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gray_track", indexes = {@Index(columnList = "serviceId"), @Index(columnList = "instanceId")})
public class GrayDynamicLogicDO {
}
