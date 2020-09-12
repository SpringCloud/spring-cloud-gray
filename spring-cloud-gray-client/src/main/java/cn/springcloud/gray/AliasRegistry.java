package cn.springcloud.gray;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-09-09 00:36
 */
public interface AliasRegistry {

    public static final String ALIAS_REGION_TYPE_SERVICE = "service";

    void removeAlias(AliasRegion aliasRegion);

    void setAliases(AliasRegion aliasRegion, String id, String... aliases);

    void removeAliases(AliasRegion aliasRegion, String... aliases);

    String removeAlias(AliasRegion aliasRegion, String alias);

    String getId(AliasRegion aliasRegion, String alias);

    void updateAliases(AliasRegion aliasRegion, String id, String[] deleteAliases, String[] addAliases);


    public static AliasRegion aliasRegion(String type, String module) {
        return new AliasRegion(type, module);
    }


    @Data
    @Builder
    @AllArgsConstructor
    public static class AliasRegion {
        private String type;
        private String module;
    }

}
