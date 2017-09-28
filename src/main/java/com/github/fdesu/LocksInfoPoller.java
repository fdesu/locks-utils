package com.github.fdesu;

import java.util.HashMap;
import java.util.Map;

public class LocksInfoPoller {

    public static Map<String, LocksInfoJmxRetriever.LockSupplementInfo> poll(
            int iterations, long sleep, String host, int port) {
        Map<String, LocksInfoJmxRetriever.LockSupplementInfo> container = new HashMap<>();
        while (iterations-- > 0) {
            System.out.println("Remaining: " + iterations);
            Map<String, LocksInfoJmxRetriever.LockSupplementInfo> dump = LocksInfoJmxRetriever.retrieve(host, port);
            for (Map.Entry<String, LocksInfoJmxRetriever.LockSupplementInfo> entry : dump.entrySet()) {
                container.merge(entry.getKey(), entry.getValue(), LocksInfoJmxRetriever.LockSupplementInfo::merge);
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            }
        }
        return container;
    }

}