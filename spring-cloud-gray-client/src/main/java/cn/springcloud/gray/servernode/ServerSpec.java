package cn.springcloud.gray.servernode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServerSpec<SERVER> {


    private String serviceId;
    private String instanceId;
    private Map<String, Object> metadata = new HashMap<>();
    private SERVER server;
    private String version;


    public void setMetadata(String name, Object value) {
        metadata.put(name, value);
    }

    public Object getMetadata(String name) {
        return metadata.get(name);
    }
}
