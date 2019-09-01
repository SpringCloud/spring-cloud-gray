package cn.springcloud.gray;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerListResult<Server> {

    private String serviceId;

    private List<Server> grayServers;
    private List<Server> normalServers;
}
