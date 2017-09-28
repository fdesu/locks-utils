package com.github.fdesu;

import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ThreadInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class LocksInfoJmxRetriever {
    public static Map<String, LockSupplementInfo> retrieve(String host, int port) {
        try {
            try (JMXConnector connect = JMXConnectorFactory.connect(new JMXServiceURL
                    ("service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi"))) {
                CompositeData[] compoisteDatas = (CompositeData[])connect.getMBeanServerConnection()
                        .invoke(new ObjectName("java.lang:type=Threading"),
                                "dumpAllThreads",
                                new Object[] {true, true},
                                new String[] {"boolean", "boolean"});
                Map<String, LockSupplementInfo> locks = new HashMap<>();
                for (CompositeData compoisteData : compoisteDatas) {
                    ThreadInfo threadInfo = ThreadInfo.from(compoisteData);
                    if (threadInfo.getLockInfo() != null) {
                        String lock = threadInfo.getLockInfo().getClassName() + "@"
                                + Integer.toHexString(threadInfo.getLockInfo().getIdentityHashCode());
                        locks.putIfAbsent(lock, new LockSupplementInfo());
                        LockSupplementInfo lockSupplementInfo = locks.get(lock);
                        lockSupplementInfo.blockedOn(threadInfo.getThreadName());
                    }

                }
                return locks;
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static final class LockSupplementInfo {
        private Map<String, Long> blockedThreadToFreq = new HashMap<>();
        private long blockedTimes;

        public void blockedOn(String blockedThread) {
            Long times = blockedThreadToFreq.putIfAbsent(blockedThread, 1L);
            if (times != null) {
                blockedThreadToFreq.put(blockedThread, times + 1);
            }
            blockedTimes++;
        }

        public static LockSupplementInfo merge(LockSupplementInfo cur, LockSupplementInfo otherInfo) {
            cur.blockedTimes += otherInfo.blockedTimes;
            for (Map.Entry<String, Long> otherEntry : otherInfo.blockedThreadToFreq.entrySet()) {
                cur.blockedThreadToFreq.merge(otherEntry.getKey(), otherEntry.getValue(), Long::sum);
            }
            return cur;
        }

        public long getBlockedTimes() {
            return blockedTimes;
        }

        public Map<String, Long> getBlockedThreadToFreq() {
            return blockedThreadToFreq;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LockSupplementInfo that = (LockSupplementInfo) o;
            return blockedTimes == that.blockedTimes && Objects.equals(blockedThreadToFreq, that.blockedThreadToFreq);
        }

        @Override
        public int hashCode() {
            return Objects.hash(blockedThreadToFreq, blockedTimes);
        }

        @Override
        public String toString() {
            return "\nLockSupplementInfo: blocked: " + blockedTimes + " times\nStats:\n"
                    + blockedThreadToFreq.entrySet().stream()
                    .map(entry -> entry.getKey() + " <=> " +  entry.getValue())
                    .collect(Collectors.joining("\n"))
                    + "\n-------------------------------\n";
        }
    }

}