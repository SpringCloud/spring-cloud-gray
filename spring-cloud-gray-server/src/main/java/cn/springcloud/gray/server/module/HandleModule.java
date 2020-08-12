package cn.springcloud.gray.server.module;

import cn.springcloud.gray.server.module.domain.Handle;
import cn.springcloud.gray.server.module.domain.HandleAction;
import cn.springcloud.gray.server.module.domain.HandleDetailInfos;
import cn.springcloud.gray.server.module.domain.query.HandleActionQuery;
import cn.springcloud.gray.server.module.domain.query.HandleQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author saleson
 * @date 2020-05-30 22:24
 */
public interface HandleModule {

    Page<Handle> queryHandles(HandleQuery handleQuery, Pageable pageable);

    List<Handle> queryHandles(HandleQuery handleQuery);

    List<Handle> findAllEnabledHandles();

    List<Handle> findHandles(HandleQuery handleQuery);

    Handle getHandle(Long handleId);

    Handle saveHandle(Handle handle);

    boolean deleteHandle(Long handleId, String userId);

    boolean recoverHandle(Long handleId, String userId);

    HandleAction saveHandleAction(HandleAction handleAction);

    boolean deleteHandleAction(Long handleActionId, String userId);

    boolean recoverHandleAction(Long handleActionId, String userId);

    HandleAction getHandleAction(Long id);

    List<HandleAction> listEnabledHandleActionsByHandleId(Long handleId);

    List<HandleAction> listHandleActionsByHandleId(Long handleId);

    Page<HandleAction> listHandleActionsByHandleId(Long handleId, Pageable pageable);

    Page<HandleAction> listHandleActions(HandleActionQuery query, Pageable pageable);

    HandleDetailInfos newHandle(HandleDetailInfos handleDetailInfos);
}
