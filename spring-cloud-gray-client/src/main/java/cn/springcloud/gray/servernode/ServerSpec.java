package cn.springcloud.gray.servernode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServerSpec {


    private String serviceId;
    private String instanceId;
    private URI uri;
    private Map<String, Object> metadatas = new HashMap<>();


    public void setMetadata(String name, Object value) {
        metadatas.put(name, value);
    }

    public Object getMetadata(String name) {
        return metadatas.get(name);
    }
}
