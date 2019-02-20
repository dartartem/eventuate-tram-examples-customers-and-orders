package io.eventuate.examples.tram.ordersandcustomers.orders;

import io.eventuate.tram.consumer.redis.TramConsumerRedisConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Import({TramConsumerRedisConfiguration.class, OrderCommonConfiguration.class})
@Profile("Redis")
public class OrderRedisConfiguration {
}
