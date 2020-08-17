package cn.springcloud.gray.server.clustering.synchro;

import lombok.Data;

import java.io.Serializable;

/**
 * @author saleson
 * @date 2020-08-14 12:31
 */
@Data
public class SynchData implements Serializable {

    private static final long serialVersionUID = -9023153501922590345L;

    private String id;
    private long timestamp = System.currentTimeMillis();
    private String dataType;

    private Object data;
}
