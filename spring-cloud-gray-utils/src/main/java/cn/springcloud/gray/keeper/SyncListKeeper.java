package cn.springcloud.gray.keeper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author saleson
 * @date 2020-08-16 04:24
 */
public class SyncListKeeper<T> implements ListKeeper<T> {

    private List<T> list;

    public SyncListKeeper() {
        this.list = new ArrayList<>();
    }

    public SyncListKeeper(List<T> list) {
        this.list = new ArrayList<>(list);
    }

    @Override
    public synchronized void add(T t) {
        List<T> newList = new ArrayList<>(this.list);
        newList.add(t);
        this.list = newList;
    }

    @Override
    public synchronized void remove(T t) {
        List<T> newList = new ArrayList<>(this.list);
        newList.remove(t);
        this.list = newList;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public List<T> values() {
        return list;
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }
}
