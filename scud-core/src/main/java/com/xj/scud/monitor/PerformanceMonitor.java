package com.xj.scud.monitor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: xiajun
 * Date: 2017/08/25 11:40
 * 性能监视器，监视每个方法的性能
 */
public class PerformanceMonitor {
    private final static Map<String, PerformanceData> monitorMap = new ConcurrentHashMap<>();

    public static void add(String methodName, int costTime) {
        PerformanceData data = monitorMap.get(methodName);
        if (data == null) {
            synchronized (PerformanceMonitor.class) {
                if ((data = monitorMap.get(methodName)) == null) {
                    data = new PerformanceData(methodName);
                    monitorMap.put(methodName, data);
                }
            }
        }
        data.add(costTime);
    }

    static Map<String, TopPercentile> monitor() {
        Map<String, TopPercentile> tpMap = new HashMap<>(monitorMap.size());
        for (String key : monitorMap.keySet()) {
            PerformanceData oldData = monitorMap.get(key);
            monitorMap.put(key, new PerformanceData(oldData.getMethodName()));
            TopPercentile tp = oldData.getTP();
            tpMap.put(key, tp);
        }
        return tpMap;
    }
}
