package cn.springcloud.gray.request;

import java.util.List;

public interface HttpGrayTrackRecordDevice {

    void record(String name, String value);

    void record(String name, List<String> values);
}
