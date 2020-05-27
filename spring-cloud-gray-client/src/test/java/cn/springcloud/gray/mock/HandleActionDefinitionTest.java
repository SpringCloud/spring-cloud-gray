package cn.springcloud.gray.mock;

import cn.springcloud.gray.choose.ChooseGroup;
import cn.springcloud.gray.model.HandleActionDefinition;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-05-22 23:07
 */
public class HandleActionDefinitionTest {

    @Test
    public void testSortList() {
        List<HandleActionDefinition> list = new ArrayList<>();
        HandleActionDefinition definition = new HandleActionDefinition();
        definition.setId("2");
        definition.setOrder(2);
        list.add(definition);

        definition = new HandleActionDefinition();
        definition.setId("1");
        definition.setOrder(1);
        list.add(definition);

        System.out.println(list.stream().sorted().collect(Collectors.toList()));
    }


    @Test
    public void test() {
        List list = Arrays.asList("1", "2")
                .stream()
                .map(j -> null)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        System.out.println(list);
    }


    @Test
    public void test1() {
        ChooseGroup chooseGroup = ChooseGroup.ofNameDefaultAll("ALL");
        System.out.println(chooseGroup);
    }
}
