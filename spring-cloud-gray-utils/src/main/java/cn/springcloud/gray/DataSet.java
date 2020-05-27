package cn.springcloud.gray;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author saleson
 * @date 2020-05-10 23:53
 */
public class DataSet<D> {

    private Set<D> datas = new ConcurrentSkipListSet<>();


    public boolean addData(D data) {
        return datas.add(data);
    }

    public boolean addDatas(Collection<D> datas) {
        return this.datas.addAll(datas);
    }

    public boolean removeData(D data) {
        return datas.remove(data);
    }

    public Set<D> getDatas() {
        return Collections.unmodifiableSet(datas);
    }

    public boolean isEmpty() {
        return datas.isEmpty();
    }
}
