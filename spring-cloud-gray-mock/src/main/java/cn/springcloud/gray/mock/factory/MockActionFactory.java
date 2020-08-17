package cn.springcloud.gray.mock.factory;

import cn.springcloud.gray.mock.MockAction;
import cn.springcloud.gray.utils.NameUtils;

/**
 * @author saleson
 * @date 2020-05-17 20:36
 */
public interface MockActionFactory<CONFIG, DEFINITION> {

    default String name() {
        return NameUtils.normalizeName(getClass(), MockActionFactory.class);
    }

    <RESULT> MockAction<RESULT> create(DEFINITION definition);

    <RESULT> MockAction<RESULT> apply(CONFIG config);


    default CONFIG newConfig() {
        throw new UnsupportedOperationException("newConfig() not implemented");
    }


}
