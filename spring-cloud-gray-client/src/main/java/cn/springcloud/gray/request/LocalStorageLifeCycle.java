package cn.springcloud.gray.request;

public interface LocalStorageLifeCycle {

    void initContext();

    void closeContext();


    public static class NoOpLocalStorageLifeCycle implements LocalStorageLifeCycle {

        @Override
        public void initContext() {
            
        }

        @Override
        public void closeContext() {

        }
    }
}
