package cn.springcloud.gray.service.test;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

public class Md5Test {

    @Test
    public void test(){
        String md5 = DigestUtils.md5Hex("admin" + "!@#DFS3df");
        System.out.println(md5);
    }
}
