package cn.springlcoud.gray.event;

import java.util.EventObject;

/**
 * @author saleson
 * @date 2020-01-30 12:41
 */
public abstract class GrayEvent<S> extends EventObject {

    private long timestamp;

    private long sortMark;


    public GrayEvent(long sortMark, S source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
        this.sortMark = sortMark;
    }


    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setSortMark(long sortMark) {
        this.sortMark = sortMark;
    }

    public long getTimestamp() {
        return timestamp;
    }


    public long getSortMark() {
        return sortMark;
    }

    @Override
    public S getSource() {
        return (S) super.getSource();
    }

    public abstract String getSourceId();

    public String getType() {
        return getClass().getSimpleName();
    }
}
