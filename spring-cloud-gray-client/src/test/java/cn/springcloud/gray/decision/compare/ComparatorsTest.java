package cn.springcloud.gray.decision.compare;

import org.junit.Assert;
import org.junit.Test;

public class ComparatorsTest {


    @Test
    public void testStringComparable(){
        Assert.assertEquals(true, Comparators.gt("1", "2"));
        Assert.assertEquals(true, Comparators.gt("a", "b"));
        Assert.assertEquals(true, Comparators.gt("A", "B"));
        Assert.assertEquals(true, Comparators.gte("b", "b"));
        Assert.assertEquals(true, Comparators.lte("b", "b"));
        Assert.assertEquals(true, Comparators.lt("b", "a"));
        Assert.assertEquals(true, Comparators.lt("2", "1"));
        Assert.assertEquals(true, Comparators.equals("2", "2"));
        Assert.assertEquals(true, Comparators.unEquals("2", "1"));
    }
}
