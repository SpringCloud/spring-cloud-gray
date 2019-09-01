package cn.springcloud.gray.choose;

import java.util.List;

public interface ListChooser<Server> {

    Server choose(List<Server> servers);
}
