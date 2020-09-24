package cn.springcloud.gray.server;

import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.oauth2.Oauth2Service;

public class GrayServerHolder {

    private static UserModule userModule;
    private static Oauth2Service oauth2Service;


    public static UserModule getUserModule() {
        return userModule;
    }

    public static void setUserModule(UserModule userModule) {
        GrayServerHolder.userModule = userModule;
    }

    public static Oauth2Service getOauth2Service() {
        return oauth2Service;
    }

    public static void setOauth2Service(Oauth2Service oauth2Service) {
        GrayServerHolder.oauth2Service = oauth2Service;
    }
}
