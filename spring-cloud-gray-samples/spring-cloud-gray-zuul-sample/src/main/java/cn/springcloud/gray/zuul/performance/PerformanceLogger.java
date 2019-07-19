package cn.springcloud.gray.zuul.performance;

import java.io.File;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PerformanceLogger {

    private static BlockingQueue<String> queue = new ArrayBlockingQueue<>(20000);
    private static Thread hook_thread;

    public static void printMethodUsedTime(String longName, long usedTime) {
        queue.add(longName + " " + usedTime);
    }

    static {
        start();
    }


    public static void start() {
        try {
            PrintWriter printWriter = new PrintWriter(new File("gray-agent-" + System.currentTimeMillis() + ".txt"));
            Thread t = new Thread(new LogRunnable(printWriter));
            t.start();
            System.out.println("开启应用...");
            hook_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("关闭应用...");
                    printWriter.flush();
                    printWriter.close();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Thread getHookThread() {
        return hook_thread;
    }


    public static class LogRunnable implements Runnable {

        private PrintWriter printWriter;

        public LogRunnable(PrintWriter printWriter) {
            this.printWriter = printWriter;
        }

        @Override
        public void run() {
            int count = 0;
            while (true) {
                try {
                    count++;
                    String v = queue.take();
                    printWriter.println(v);
                    if (count > 9999) {
                        printWriter.flush();
                        count = 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
