package com.lrwh.redis.sentinel.exporter.prometheus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author liurui
 * @date 2022/4/27 11:13
 */
@Component
public class RedisMetricsScheduler {

    private static final Logger logger = LoggerFactory.getLogger(RedisMetricsScheduler.class);
    @Autowired
    private RedisTemplate redisTemplate;

    private static RedisSentinelConnection connection;



    @PostConstruct
    @Scheduled(cron = "${cron}")
    public void initRedis() {
        if (connection==null){
            connection = redisTemplate.getConnectionFactory().getSentinelConnection();
        }
        for (RedisServer master : connection.masters()) {
            buildMaster(master);
        }
    }

    public void buildMaster(RedisServer master) {

        List<String> tags = new ArrayList<>();
        tags.add(master.getHost());
        tags.add(master.getPort().toString());
        tags.add(master.getName());

        String[] master_tags = tags.stream().toArray(String[]::new);
        init(master,master_tags);

        long reply = Long.valueOf(master.get("last-ping-reply"));
        long sent = master.getLastPingSent();
        long ok_reply =  Long.valueOf(master.get("last-ok-ping-reply"));

        MetricDefinition._redis_sentinel_last_ok_ping_latency.labels(master_tags).set(reply - ok_reply);
        MetricDefinition._redis_sentinel_ping_latency.labels(master_tags).set(reply-sent);
        MetricDefinition._redis_sentinel_known_slaves.labels(master_tags).set(master.getNumberSlaves());
        MetricDefinition._redis_sentinel_known_sentinels.labels(master_tags).set(master.getNumberOtherSentinels()+1);
        MetricDefinition._redis_sentinel_link_pending_commands.labels(master_tags).set(Optional.ofNullable(master.getPendingCommands()).orElse(0L));
        MetricDefinition._redis_sentinel_cluster_type.labels(master_tags).set(Constants.NodeType.MASTER.v);
        MetricDefinition._redis_sentinel_node_state.labels(master_tags).set(1L);
        logger.info("master - {}:{},{},{},{}", master.getHost(), master.getPort(), master.getNumberSlaves(), master.getNumberOtherSentinels(), master.getFlags());
        buildSlaves(master,master_tags);
    }

    public void buildSlaves(RedisServer master,String[] master_tags) {
        long slaves_odown = 0L;
        long slaves_sdown = 0L;

        Collection<RedisServer> slaves = connection.slaves(master);
        for (RedisServer redisServer : slaves) {
            List<String> tags = new ArrayList<>();
            tags.add(redisServer.getHost());
            tags.add(redisServer.getPort().toString());
            tags.add(master.getName());
            String[] slave_tags = tags.stream().toArray(String[]::new);

            String flags = redisServer.getFlags();


            if (flags.contains("s_down")) {
                slaves_sdown += 1L;
            } else if (flags.contains("is_odown")) {
                slaves_odown += 1L;
            }

            if (flags.contains("disconnected")){
                MetricDefinition._redis_sentinel_node_state.labels(slave_tags).set(0L);
            }else{
                MetricDefinition._redis_sentinel_node_state.labels(slave_tags).set(1L);
            }

            logger.info("slaves - {}:{},{},{}", redisServer.getHost(), redisServer.getPort(), redisServer.getDownAfterMilliseconds(), redisServer.getFlags());
            MetricDefinition._redis_sentinel_cluster_type.labels(slave_tags).set(Constants.NodeType.SLAVE.v);
            MetricDefinition._redis_sentinel_link_pending_commands.labels(slave_tags).set(Optional.ofNullable(redisServer.getPendingCommands()).orElse(0L));
        }
        MetricDefinition._redis_sentinel_ok_slaves.labels(master.getName()).set(slaves.size()-slaves_sdown);
        MetricDefinition._redis_sentinel_odown_slaves.labels(master_tags).set(slaves_odown);
        MetricDefinition._redis_sentinel_sdown_slaves.labels(master_tags).set(slaves_sdown);
    }


    /**
     * 初始化Gauge指标
     * @param master
     * @param master_tags
     */
    private void init(RedisServer master,String[] master_tags){
        MetricDefinition._redis_sentinel_odown_slaves.labels(master_tags).set(0);
        MetricDefinition._redis_sentinel_sdown_slaves.labels(master_tags).set(0);
        MetricDefinition._redis_sentinel_known_slaves.labels(master_tags).set(0);
        MetricDefinition._redis_sentinel_known_sentinels.labels(master_tags).set(0);
        MetricDefinition._redis_sentinel_link_pending_commands.labels(master_tags).set(0);
        MetricDefinition._redis_sentinel_ok_slaves.labels(master.getName()).set(0);
    }

}
