package cn.springcloud.gray.changed.notify;

import cn.springcloud.gray.retriever.GenericRetriever;

import java.util.Collection;
import java.util.List;

/**
 * @author saleson
 * @date 2020-06-01 10:35
 */
public class DefaultChangedNotifyDriver implements ChangedNotifyDriver {

    private GenericRetriever<ChangedListener> changedListenerGenericRetriever;


    public DefaultChangedNotifyDriver() {
        this.changedListenerGenericRetriever = new GenericRetriever<>(ChangedListener.class);
    }

    public DefaultChangedNotifyDriver(List<ChangedListener> changedListeners) {
        this.changedListenerGenericRetriever = new GenericRetriever<>(changedListeners, ChangedListener.class);
    }


    @Override
    public void registerListener(ChangedListener changedListener) {
        changedListenerGenericRetriever.addFunction(changedListener);
    }

    @Override
    public void registerListeners(Collection<ChangedListener> changedListeners) {
        changedListenerGenericRetriever.addFunctions(changedListeners);
    }

    @Override
    public void chaned(ChangedType changedType, Object source) {
        List<ChangedListener> changedListeners = getListeners(source.getClass());
        switch (changedType) {
            case ADDED:
                changedListeners.forEach(changedListener -> changedListener.addedNotify(source));
                break;
            case UPDATED:
                changedListeners.forEach(changedListener -> changedListener.updatedNotify(source));
                break;
            case DELETED:
                changedListeners.forEach(changedListener -> changedListener.deletedNotify(source));
                break;
        }
    }

    @Override
    public void clearAll(Class sourceClass) {
        List<ChangedListener> changedListeners = getListeners(sourceClass);
        changedListeners.forEach(ChangedListener::clearAll);
    }

    private List<ChangedListener> getListeners(Class sourceClass) {
        return changedListenerGenericRetriever.retrieveFunctions(sourceClass);
    }

}
