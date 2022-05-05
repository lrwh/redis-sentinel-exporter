redis-sentinel-exporter for prometheus

## application.yaml参数

```yaml
spring:
  application:
    name: redis-sentinel-exporter
  redis:
    sentinel:
      # 集群名称
      master: mymaster
      # 哨兵节点地址
      nodes: 127.0.0.1:26379,127.0.0.1:26380,127.0.0.1:26381
    client-type: lettuce
server:
  port: 6390

# 指标采集周期
cron: "*/3 * * * * ?"
```



## 指标定义

| 指标                                   | 含义             | 类型    |
| ------------------------------------ | -------------- | ----- |
| redis_sentinel_known_sentinels       | sentinels实例数   | Gauge |
| redis_sentinel_known_slaves          | 集群slaves实例数    | Gauge |
| redis_sentinel_cluster_type          | 集群节点类型         | Gauge |
| redis_sentinel_link_pending_commands | sentinel挂起命令数  | Gauge |
| redis_sentinel_odown_slaves          | slave客观宕机      | Gauge |
| redis_sentinel_sdown_slaves          | slave主观宕机      | Gauge |
| redis_sentinel_ok_slaves             | 正在运行的slave数    | Gauge |
| redis_sentinel_ping_latency          | 哨兵ping的延迟显示为毫秒 | Gauge |
| redis_sentinel_last_ok_ping_latency  | 哨兵ping成功的秒数    | Gauge |








