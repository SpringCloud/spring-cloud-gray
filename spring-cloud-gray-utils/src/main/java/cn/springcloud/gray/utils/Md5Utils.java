package cn.springcloud.gray.utils;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author saleson
 * @date 2020-03-22 13:27
 */
public class Md5Utils {


    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }
}
