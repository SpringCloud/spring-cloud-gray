package cn.springcloud.bamboo;


import javax.servlet.http.HttpServletRequest;

/**
 * 请求的body
 * 如读取 {@link HttpServletRequest#getInputStream()}获取得到的内容
 */
public interface RequestBody {


    /**
     * 返回byte数组类型的request body
     * @return 请求体内容
     */
    byte[] getBody();


    /**
     * 返回字符串类型的request body
     * @return 请求体内容
     */
    String getBodyString();


    /**
     * 将byte数组类型的request body转换成字符串类型的request body并返回
     * @param charset 字符串编码
     * @return 请求体内容
     */
    String getBodyString(String charset);
}
