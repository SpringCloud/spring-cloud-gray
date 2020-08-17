package cn.springcloud.gray.servernode;

import java.util.Map;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-11 23:40
 */
public class MetadataVersionExtractor<SERVER> implements VersionExtractor<SERVER> {

    private String versionField;


    public MetadataVersionExtractor() {
        this("version");
    }

    public MetadataVersionExtractor(String versionField) {
        this.versionField = versionField;
    }

    @Override
    public String getVersion(String serviceId, SERVER server, Map metadata) {
        if (Objects.isNull(metadata)) {
            return null;
        }
        Object version = metadata.get(versionField);
        return Objects.isNull(version) ? null : String.valueOf(version);
    }
}
