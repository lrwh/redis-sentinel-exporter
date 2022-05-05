package com.lrwh.redis.sentinel.exporter.prometheus;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liurui
 * @date 2022/4/28 10:09
 */
public class Constants {
    public static ConcurrentHashMap<String, List<String>> cluster_nodes = new ConcurrentHashMap<String, List<String>>();

    enum NodeType {
        MASTER(1L, "master", "主节点"),
        SLAVE(0L, "slave", "从节点");
        Long v;
        String type;

        NodeType(Long v, String type, String descr) {
            this.v = v;
            this.type = type;
        }
    }
}
