package cn.springcloud.gray.model;

/**
 * @author saleson
 * @date 2020-05-24 22:49
 */
public enum HandleRuleType {
    MOCK_APPLICATION_RESPONSE("mock_application_response"),
    MOCK_SERVER_CLIENT_REPSONSE("mock_server_client_response");


    private String code;

    HandleRuleType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
