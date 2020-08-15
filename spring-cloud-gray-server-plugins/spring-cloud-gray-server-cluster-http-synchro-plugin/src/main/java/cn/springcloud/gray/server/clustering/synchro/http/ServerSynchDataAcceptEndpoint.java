package cn.springcloud.gray.server.clustering.synchro.http;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.clustering.synchro.SynchData;
import cn.springcloud.gray.server.clustering.synchro.SynchDataAcceptor;
import cn.springcloud.gray.server.utils.ApiResHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-08-15 23:01
 */
@RestController
@Slf4j
public class ServerSynchDataAcceptEndpoint {

    public static final String ENDPOING_URI = "/server/synch/accept";

    @Autowired
    private SynchDataAcceptor synchDataAcceptor;

    @RequestMapping(value = ENDPOING_URI, method = RequestMethod.POST)
    public ApiRes<Void> accept(@RequestBody HttpSynchSignal synchSignal) {
        log.info("接收到同步数据,Synch Signal Id:{}, 开始转换同步数据对象", synchSignal.getId());
        byte[] objBytes = synchSignal.getObjectBytes();

        if (Objects.isNull(objBytes)) {
            log.error("Synch Signal Id:{} objBytes is null", synchSignal.getId());
            return ApiResHelper.failed("objBytes is null");
        }
        if (objBytes.length < 1) {
            log.error("Synch Signal Id:{} HttpSynchSignal.objectBytes length is 0", synchSignal.getId());
            return ApiResHelper.failed("HttpSynchSignal.objectBytes length is 0");
        }

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(objBytes))) {
            SynchData synchData = (SynchData) ois.readObject();
            log.info("Synch Signal Id:{} 同步数据对象转换成功");
            synchDataAcceptor.accept(synchData);
            log.info("Synch Signal Id:{} 同步数据对象接收完成");
        } catch (IOException e) {
            log.error("Synch Signal Id:{}，发生IOException", synchSignal.getId(), e);
            return ApiResHelper.failed("接收到同步数据，发生IOException");
        } catch (ClassNotFoundException e) {
            log.error("Synch Signal Id:{}，发生ClassNotFoundException", synchSignal.getId(), e);
            return ApiResHelper.failed("接收到同步数据，发生ClassNotFoundException");
        } catch (ClassCastException e) {
            log.error("Synch Signal Id:{}，发生ClassNotFoundException", synchSignal.getId(), e);
            return ApiResHelper.failed("接收到同步数据，发生ClassNotFoundException");
        }
        return ApiResHelper.success();
    }
}
