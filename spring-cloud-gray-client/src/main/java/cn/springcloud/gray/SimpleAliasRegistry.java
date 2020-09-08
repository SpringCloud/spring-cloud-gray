package cn.springcloud.gray;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-09-09 00:43
 */
public class SimpleAliasRegistry implements AliasRegistry {

    private Map<AliasRegion, Map<String, String>> modules = new HashMap<>();

    @Override
    public void removeAlias(AliasRegion aliasRegion) {
        if (!modules.containsKey(aliasRegion)) {
            return;
        }
        remove(aliasRegion);
    }

    @Override
    public void setAliases(AliasRegion aliasRegion, String id, String... aliases) {
        if (Objects.isNull(aliases)) {
            return;
        }
        synchronized (this) {
            Map<String, String> aliasMap = modules.get(aliasRegion);
            Map<String, String> newAliasMap = Objects.isNull(aliasMap) ? new HashMap<>() : new HashMap<>(aliasMap);
            for (String alias : aliases) {
                newAliasMap.put(alias, id);
            }
            if (Objects.isNull(aliasMap)) {
                Map<AliasRegion, Map<String, String>> newModules = new HashMap<>(modules);
                newModules.put(aliasRegion, newAliasMap);
                this.modules = newModules;
            } else {
                modules.put(aliasRegion, newAliasMap);
            }
        }
    }

    @Override
    public void removeAliases(AliasRegion aliasRegion, String... aliases) {
        if (Objects.isNull(aliases)) {
            return;
        }
        Map<String, String> aliasMap = modules.get(aliasRegion);
        if (Objects.isNull(aliasMap)) {
            return;
        }
        synchronized (this) {
            Map<String, String> newAliasMap = new HashMap<>(aliasMap);
            for (String alias : aliases) {
                newAliasMap.remove(alias);
            }
            setNewAliasMap(aliasRegion, newAliasMap);
        }
    }

    @Override
    public String removeAlias(AliasRegion aliasRegion, String alias) {
        Map<String, String> aliasMap = modules.get(aliasRegion);
        if (Objects.isNull(aliasMap)) {
            return null;
        }
        synchronized (this) {
            Map<String, String> newAliasMap = new HashMap<>(aliasMap);
            String id = newAliasMap.remove(alias);
            setNewAliasMap(aliasRegion, newAliasMap);
            return id;
        }
    }

    @Override
    public String getId(AliasRegion aliasRegion, String alias) {
        Map<String, String> aliasMap = modules.get(aliasRegion);
        return Objects.isNull(aliasMap) ? null : aliasMap.get(alias);
    }

    @Override
    public void updateAliases(AliasRegion aliasRegion, String id, String[] deleteAliases, String[] addAliases) {
        if (Objects.isNull(deleteAliases) && Objects.isNull(addAliases)) {
            return;
        }
        synchronized (this) {
            Map<String, String> aliasMap = modules.get(aliasRegion);
            Map<String, String> newAliasMap = new HashMap<>();
            if (Objects.nonNull(aliasMap)) {
                newAliasMap.putAll(aliasMap);
            }
            if (Objects.nonNull(deleteAliases)) {
                for (String deleteAlias : deleteAliases) {
                    newAliasMap.remove(deleteAlias);
                }
            }
            if (Objects.nonNull(addAliases)) {
                for (String addAlias : addAliases) {
                    newAliasMap.put(addAlias, id);
                }
            }
            setNewAliasMap(aliasRegion, newAliasMap);
        }
    }


    protected synchronized void setNewAliasMap(AliasRegion aliasRegion, Map<String, String> newAliasMap) {
        if (newAliasMap.isEmpty()) {
            remove(aliasRegion);
        } else {
            modules.put(aliasRegion, newAliasMap);
        }
    }

    protected synchronized void remove(AliasRegion aliasRegion) {
        Map<AliasRegion, Map<String, String>> newModules = new HashMap<>(modules);
        newModules.remove(aliasRegion);
        this.modules = newModules;
    }
}
