package cn.springcloud.gray.changed.notify;

/**
 * @author saleson
 * @date 2020-06-01 07:56
 */
public interface ChangedListener<S> {

    void addedNotify(S source);

    void updatedNotify(S source);

    void deletedNotify(S source);

    void clearAll();

}
