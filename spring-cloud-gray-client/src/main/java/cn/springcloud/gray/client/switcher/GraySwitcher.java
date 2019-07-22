package cn.springcloud.gray.client.switcher;

public interface GraySwitcher {

    boolean judge();


    public static class DefaultGraySwitcher implements GraySwitcher {

        @Override
        public boolean judge() {
            return true;
        }
    }
}
