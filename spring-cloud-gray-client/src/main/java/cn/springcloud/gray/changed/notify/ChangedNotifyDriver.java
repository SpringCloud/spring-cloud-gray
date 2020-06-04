package cn.springcloud.gray.changed.notify;

import java.util.Collection;

/**
 * @author saleson
 * @date 2020-06-01 07:52
 */
public interface ChangedNotifyDriver {


    void registerListener(ChangedListener changedListener);


    void registerListeners(Collection<ChangedListener> changedListeners);

    void chaned(ChangedType changedType, Object source);


    void clearAll(Class sourceClass);

}
