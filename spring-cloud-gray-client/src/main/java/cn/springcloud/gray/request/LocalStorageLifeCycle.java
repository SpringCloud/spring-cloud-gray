package cn.springcloud.gray.request;

public interface LocalStorageLifeCycle {

    void initContext();

    void initContext(String mark);

    void closeContext();

    void closeContext(String mark);


    public static class NoOpLocalStorageLifeCycle implements LocalStorageLifeCycle {

        @Override
        public void initContext() {

        }

        @Override
        public void initContext(String mark) {

        }

        @Override
        public void closeContext() {

        }

        @Override
        public void closeContext(String mark) {

        }
    }
}
