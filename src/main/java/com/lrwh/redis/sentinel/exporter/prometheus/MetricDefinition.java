package com.lrwh.redis.sentinel.exporter.prometheus;

import io.prometheus.client.Gauge;

/**
 * @author liurui
 * @date 2022/4/27 10:59
 */
public class MetricDefinition {
    public static final Gauge _redis_sentinel_known_sentinels = Gauge.build()
            .name("redis_sentinel_known_sentinels")
            .help("number of sentinels detected Shown as instance")
            .labelNames("ip","port","redis_cluster_name")
            .register();
    public static final Gauge _redis_sentinel_known_slaves = Gauge.build()
            .name("redis_sentinel_known_slaves")
            .help("number of slaves detected Shown as instance")
            .labelNames("ip","port","redis_cluster_name")
            .register();
    public static final Gauge _redis_sentinel_node_state = Gauge.build()
            .name("redis_sentinel_node_state")
            .help("nodes state for master and slave")
            .labelNames("ip","port","redis_cluster_name")
            .register();
    public static final Gauge _redis_sentinel_link_pending_commands = Gauge.build()
            .name("redis_sentinel_link_pending_commands")
            .help("number of pending sentinel commands.Shown as command")
            .labelNames("ip","port","redis_cluster_name")
            .register();
    public static final Gauge _redis_sentinel_cluster_type = Gauge.build()
            .name("redis_sentinel_cluster_type")
            .help("sentinel cluster type Shown as instance")
            .labelNames("ip","port","redis_cluster_name")
            .register();
    public static final Gauge _redis_sentinel_odown_slaves = Gauge.build()
            .name("redis_sentinel_odown_slaves")
            .help("number of slaves that are in the Objectively Down state . Shown as instance")
            .labelNames("ip","port","redis_cluster_name")
            .register();
    public static final Gauge _redis_sentinel_sdown_slaves = Gauge.build()
            .name("redis_sentinel_sdown_slaves")
            .help("number of slaves that are in the Subjectively Down state. Shown as instance")
            .labelNames("ip","port","redis_cluster_name")
            .register();
    public static final Gauge _redis_sentinel_ok_slaves = Gauge.build()
            .name("redis_sentinel_ok_slaves")
            .help("number of slaves up and running . Shown as instance")
            .labelNames("redis_cluster_name")
            .register();
    public static final Gauge _redis_sentinel_ping_latency = Gauge.build()
            .name("redis_sentinel_ping_latency")
            .help("latency of a sentinel ping.Shown as millisecond")
            .labelNames("ip","port","redis_cluster_name")
            .register();
    public static final Gauge _redis_sentinel_last_ok_ping_latency = Gauge.build()
            .name("redis_sentinel_last_ok_ping_latency")
            .help("number of seconds since last OK ping .Shown as second")
            .labelNames("ip","port","redis_cluster_name")
            .register();
}
