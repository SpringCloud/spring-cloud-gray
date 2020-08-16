package cn.springcloud.gray.server.clustering.synchro.http;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.clustering.ServerCluster;
import cn.springcloud.gray.server.clustering.synchro.ServerSynchronizer;
import cn.springcloud.gray.server.clustering.synchro.SynchData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author saleson
 * @date 2020-08-15 18:06
 */
@Slf4j
public class HttpServerSynchronizer implements ServerSynchronizer {

    private int peerNodeSynchronizeRetryTimes;
    private RestTemplate rest;
    private ServerCluster serverCluster;
    private ExecutorService executorService;

    public HttpServerSynchronizer(ServerCluster serverCluster) {
        this(serverCluster, new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10)));
    }

    public HttpServerSynchronizer(ServerCluster serverCluster, ExecutorService executorService) {
        this(new RestTemplate(), serverCluster, executorService);
    }

    public HttpServerSynchronizer(RestTemplate rest, ServerCluster serverCluster, ExecutorService executorService) {
        this(3, rest, serverCluster, executorService);
    }

    public HttpServerSynchronizer(int peerNodeSynchronizeRetryTimes, RestTemplate rest, ServerCluster serverCluster, ExecutorService executorService) {
        this.peerNodeSynchronizeRetryTimes = peerNodeSynchronizeRetryTimes;
        this.rest = rest;
        this.serverCluster = serverCluster;
        this.executorService = executorService;
    }

    @Override
    public void broadcast(SynchData synchData) {
        String[] peerNodes = serverCluster.exceptItselfPeerNodesUrls();
        if (ArrayUtils.isEmpty(peerNodes)) {
            return;
        }
        executorService.execute(() -> {
            if (StringUtils.isEmpty(synchData.getId())) {
                synchData.setId(StringUtils.defaultString(synchData.getDataType()) +
                        cn.springcloud.gray.utils.StringUtils.generateUUID());
            }

            broadcastSynchData(synchData, peerNodes);
        });
    }

    private void broadcastSynchData(SynchData synchData, String[] peerNodes) {
        HttpSynchSignal synchSignal = createHttpSynchSignal(synchData);
        if (Objects.isNull(synchSignal)) {
            return;
        }
        for (String peerNode : peerNodes) {
            try {
                sendHttpSignal(synchSignal, peerNode);
            } catch (Exception e) {
                log.error("Synch Signal Id:{}, 发往->{}, 同步失败:{}", synchSignal.getId(), peerNode, e.getMessage(), e);
            }
        }
    }

    private HttpSynchSignal createHttpSynchSignal(SynchData synchData) {
        byte[] obs = null;
        try {
            obs = synchDataToBytes(synchData);
        } catch (IOException e) {
            log.error("Synch Signal Id:{} 同步数据转字节失败:{}", synchData.getId(), e.getMessage(), e);
            return null;
        }
        HttpSynchSignal synchSignal = new HttpSynchSignal();
        synchSignal.setId(synchData.getId());
        synchSignal.setObjectBytes(obs);
        return synchSignal;
    }

    private byte[] synchDataToBytes(SynchData synchData) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(synchData);
        return os.toByteArray();
    }

    private void sendHttpSignal(HttpSynchSignal synchSignal, String peerNode) {
        log.info("开始发送同步数据, Synch Signal Id:{}, 发往->{}", synchSignal.getId(), peerNode);

        for (int sendTimes = 1; sendTimes <= peerNodeSynchronizeRetryTimes; sendTimes++) {
            ApiRes apiRes = rest.postForObject(peerNode + ServerSynchDataAcceptEndpoint.ENDPOING_URI, synchSignal, ApiRes.class);
            if (apiRes.judgeSuccess()) {
                log.info("发送同步数据成功, Synch Signal Id:{}, 发往->{}", synchSignal.getId(), peerNode);
                return;
            }
            log.warn("Synch Signal Id:{}, 发往->{}, 第{}次同步失败, 原因:{}",
                    synchSignal.getId(), peerNode, sendTimes, apiRes.getMessage());
        }
        log.error("Synch Signal Id:{}, 发往->{}, 共尝试{}次同步失败", synchSignal.getId(), peerNode, peerNodeSynchronizeRetryTimes);
    }


}
