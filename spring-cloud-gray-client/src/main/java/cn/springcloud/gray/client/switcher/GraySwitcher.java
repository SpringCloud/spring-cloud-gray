package cn.springcloud.gray.client.switcher;

public interface GraySwitcher {

    boolean state();

    boolean isEanbleGrayRouting();


    public static class DefaultGraySwitcher implements GraySwitcher {

        @Override
        public boolean state() {
            return true;
        }

        @Override
        public boolean isEanbleGrayRouting() {
            return true;
        }
    }
}
