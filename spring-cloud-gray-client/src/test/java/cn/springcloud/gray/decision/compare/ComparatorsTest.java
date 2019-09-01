package cn.springcloud.gray.decision.compare;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ComparatorsTest {

    @Test
    public void test(){

        List<String> list1 = new ArrayList<>();
        list1.add("4.12.0");
        list1.add("4.13.0");
        List<String> list2 = new ArrayList<>();
        list1.add("4.13.0");
        list1.add("4.11.0");
        boolean res = Comparators.getCollectionStringComparator(CompareMode.NOT_CONTAINS_ALL)
                .test(list1, list2);
        Assert.assertTrue(!res);


        List<String> lista1 = new ArrayList<>();
        lista1.add("4.12.0");
        lista1.add("4.13.0");
        List<String> lista2 = new ArrayList<>();
        lista2.add("4.10.0");
        lista2.add("4.11.0");
        boolean res1 = Comparators.getCollectionStringComparator(CompareMode.NOT_CONTAINS_ALL)
                .test(lista1, lista2);
        Assert.assertTrue(res1);



        List<String> listb1 = new ArrayList<>();
        listb1.add("4.12.0");
        listb1.add("4.13.0");
        List<String> listb2 = new ArrayList<>();
        listb2.add("4.11.0");
        listb2.add("4.13.0");
        boolean resb = Comparators.getCollectionStringComparator(CompareMode.NOT_CONTAINS_ANY)
                .test(listb1, listb2);
        Assert.assertTrue(!resb);

        List<String> listc1 = new ArrayList<>();
        listc1.add("4.12.0");
        listc1.add("4.13.0");
        List<String> listc2 = new ArrayList<>();
        listc2.add("4.11.0");
        listb2.add("4.12.0");
        boolean resc = Comparators.getCollectionStringComparator(CompareMode.NOT_CONTAINS_ANY)
                .test(listc1, listc2);
        Assert.assertTrue(resc);
    }
}
