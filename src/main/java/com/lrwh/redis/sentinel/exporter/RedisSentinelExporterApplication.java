package com.lrwh.redis.sentinel.exporter;

import io.lettuce.core.ReadFrom;
import io.prometheus.client.exporter.MetricsServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableScheduling
@RestController
public class RedisSentinelExporterApplication {

    @Bean
    public LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer() {
        return builder -> builder.readFrom(ReadFrom.REPLICA);
    }

    /**
     * Expose Prometheus metrics.
     */
    @Bean
    public ServletRegistrationBean<MetricsServlet> metricsServlet() {
        ServletRegistrationBean<MetricsServlet> bean = new ServletRegistrationBean<>(new MetricsServlet(), "/metrics");
        bean.setLoadOnStartup(1);
        return bean;
    }
    public static void main(String[] args) {
        SpringApplication.run(RedisSentinelExporterApplication.class, args);
    }

}
