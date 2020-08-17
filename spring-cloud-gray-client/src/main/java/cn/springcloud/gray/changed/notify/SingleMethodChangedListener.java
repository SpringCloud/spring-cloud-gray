package cn.springcloud.gray.changed.notify;

/**
 * @author saleson
 * @date 2020-06-01 08:00
 */
public abstract class SingleMethodChangedListener<S> implements ChangedListener<S> {


    @Override
    public void addedNotify(S source) {
        changedNotify(ChangedType.ADDED, source);
    }

    @Override
    public void updatedNotify(S source) {
        changedNotify(ChangedType.UPDATED, source);
    }

    @Override
    public void deletedNotify(S source) {
        changedNotify(ChangedType.DELETED, source);
    }


    protected abstract void changedNotify(ChangedType changedType, S source);
}
