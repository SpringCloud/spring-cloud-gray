package cn.springcloud.gray.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Setter
@Getter
public class GrayTrackDefinition implements Serializable {


    private static final long serialVersionUID = -968308788323949943L;
    private String name;

    private String value;
}
