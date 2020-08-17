package cn.springcloud.gray.refresh;

/**
 * @author saleson
 * @date 2019-12-12 16:02
 */
public interface Refresher {

    boolean refresh();

    boolean load();


    String triggerName();


}
