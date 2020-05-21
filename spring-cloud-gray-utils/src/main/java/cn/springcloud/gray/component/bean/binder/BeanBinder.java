package cn.springcloud.gray.component.bean.binder;

/**
 * @author saleson
 * @date 2020-05-17 20:50
 */
public interface BeanBinder<INFO> {

    void binding(Object obj, INFO infos);

}
