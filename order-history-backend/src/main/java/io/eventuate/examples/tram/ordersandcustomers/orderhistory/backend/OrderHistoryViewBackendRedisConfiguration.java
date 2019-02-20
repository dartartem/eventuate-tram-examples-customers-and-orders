package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.tram.consumer.redis.TramConsumerRedisConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Import({OrderHistoryViewBackendCommonConfiguration.class, TramConsumerRedisConfiguration.class})
@Profile("Redis")
public class OrderHistoryViewBackendRedisConfiguration {
}
