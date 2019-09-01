package cn.springcloud.gray.client.switcher;

public interface GraySwitcher {

    boolean state();


    public static class DefaultGraySwitcher implements GraySwitcher {

        @Override
        public boolean state() {
            return true;
        }
    }
}
