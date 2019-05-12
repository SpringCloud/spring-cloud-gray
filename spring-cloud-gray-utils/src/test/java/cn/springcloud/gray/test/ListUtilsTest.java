package cn.springcloud.gray.test;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.ListUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListUtilsTest {

    @Test
    public void testIsEqualList() {

        List<String> a = Arrays.asList("a", "b");
        List<String> b = Arrays.asList("b", "a");


        Collections.sort(a);
        Collections.sort(b);

        boolean c = ListUtils.isEqualList(a, b);
        System.out.println(c);
    }

    @Test
    public void testContainsAny() {
        List<String> a = Arrays.asList("a", "b", "c");
        List<String> b = Arrays.asList("b", "a", "d");
        boolean c = CollectionUtils.containsAny(a, b);
        System.out.println(c);
    }

    @Test
    public void testContainsAll() {
        List<String> a = Arrays.asList("a", "b", "c");
        List<String> b = Arrays.asList("b", "a");
        boolean c = a.containsAll(b);
        System.out.println(c);
    }
}
