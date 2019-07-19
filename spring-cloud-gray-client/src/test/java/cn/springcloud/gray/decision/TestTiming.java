package cn.springcloud.gray.decision;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class TestTiming {

    private AtomicLong count = new AtomicLong();
    private long startTimeMs;
    private long endTimeMs;


    public long increaseTimes() {
        return count.incrementAndGet();
    }

    public long getInvokeCount() {
        return count.get();
    }

    public void start() {
        if (startTimeMs == 0) {
            startTimeMs = System.currentTimeMillis();
        }
    }

    public void restart() {
        startTimeMs = System.currentTimeMillis();
    }

    public void stop() {
        endTimeMs = System.currentTimeMillis();
    }

    public long getUsedTime() {
        return endTimeMs - startTimeMs;
    }

    public BigDecimal calAvgUsedTime() {
        long count = getInvokeCount();
        if (count == 0) {
            count = 1;
        }
        return new BigDecimal(getUsedTime()).divide(new BigDecimal(count));
    }

    public double getAvgUsedTime() {
        return calAvgUsedTime().doubleValue();
    }


    public Map<String, Object> result2map() {
        Map<String, Object> result = new HashMap<>();
        result.put("usedTime", getUsedTime());
        result.put("invokedCount", getInvokeCount());
        result.put("avgUsedTime", calAvgUsedTime().toString());
        return result;
    }

}
