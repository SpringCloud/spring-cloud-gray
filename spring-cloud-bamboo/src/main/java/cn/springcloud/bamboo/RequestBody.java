package cn.springcloud.bamboo;

public interface RequestBody {



    byte[] getBody();

    String getBodyString();

    String getBodyString(String charset);
}
