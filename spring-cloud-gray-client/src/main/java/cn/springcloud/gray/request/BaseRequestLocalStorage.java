package cn.springcloud.gray.request;

/**
 * @author saleson
 * @date 2020-08-22 10:07
 */
public abstract class BaseRequestLocalStorage implements RequestLocalStorage {

    private LocalStorageLifeCycle localStorageLifeCycle;

    public BaseRequestLocalStorage(LocalStorageLifeCycle localStorageLifeCycle) {
        this.localStorageLifeCycle = localStorageLifeCycle;
    }

    @Override
    public LocalStorageLifeCycle getLocalStorageLifeCycle() {
        return localStorageLifeCycle;
    }

}
