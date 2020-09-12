package cn.springcloud.gray.request;

import java.util.List;

public interface GrayTrackRecordDevice {

    void record(String name, String value);

    void record(String name, List<String> values);
}
