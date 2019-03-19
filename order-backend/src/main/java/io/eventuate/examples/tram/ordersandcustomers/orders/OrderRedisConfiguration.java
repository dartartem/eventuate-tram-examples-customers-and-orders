package io.eventuate.examples.tram.ordersandcustomers.orders;

import io.eventuate.tram.consumer.redis.TramConsumerRedisConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TramConsumerRedisConfiguration.class, OrderCommonConfiguration.class})
public class OrderRedisConfiguration {
}
