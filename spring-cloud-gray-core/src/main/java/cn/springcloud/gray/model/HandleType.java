package cn.springcloud.gray.model;

/**
 * @author saleson
 * @date 2020-05-24 22:40
 */
public enum HandleType {
    MOCK("mock");

    private String code;

    HandleType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
