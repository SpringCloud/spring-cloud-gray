package cn.springcloud.gray.server;

import cn.springcloud.gray.server.module.user.UserModule;

public class GrayServerHolder {

    private static UserModule userModule;


    public static UserModule getUserModule() {
        return userModule;
    }

    public static void setUserModule(UserModule userModule) {
        GrayServerHolder.userModule = userModule;
    }
}
