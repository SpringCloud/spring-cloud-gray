package cn.springcloud.gray.mock;

import cn.springcloud.gray.changed.notify.ChangedType;
import cn.springcloud.gray.changed.notify.SingleMethodChangedListener;
import cn.springcloud.gray.handle.HandleInfo;

/**
 * @author saleson
 * @date 2020-06-01 11:23
 */
public class MockHandleChangedListener extends SingleMethodChangedListener<HandleInfo> {

    private MockManager mockManager;

    public MockHandleChangedListener(MockManager mockManager) {
        this.mockManager = mockManager;
    }

    @Override
    protected void changedNotify(ChangedType changedType, HandleInfo source) {
        mockManager.invalidateCache(source.getId());
    }

    @Override
    public void clearAll() {
        mockManager.invalidateAllCache();
    }
}
