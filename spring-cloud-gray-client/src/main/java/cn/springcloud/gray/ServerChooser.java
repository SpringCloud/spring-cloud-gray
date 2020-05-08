package cn.springcloud.gray;

import cn.springcloud.gray.choose.ListChooser;

import java.util.List;

public interface ServerChooser<Server> {


    Server chooseServer(List<Server> servers, ListChooser<Server> chooser);

}
