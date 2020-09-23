package cn.springcloud.gray.event;

import org.apache.commons.lang3.ClassUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author saleson
 * @date 2020-01-30 15:33
 */
public class ClassAssignableTest {

    @Test
    public void test() {
        boolean v = ClassUtils.isAssignable(TGrayInstanceEvent.class, GrayEvent.class);
        Assert.assertTrue(v);
        v = ClassUtils.isAssignable(TGrayPolicyEvent.class, GrayEvent.class);
        Assert.assertTrue(v);
    }
}
