package cn.springcloud.gray.choose;

import java.util.List;

public interface ServerChooser<Server> {


    Server chooseServer(List<Server> servers, ListChooser<Server> chooser);

}
