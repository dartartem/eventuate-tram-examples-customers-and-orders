package io.eventuate.examples.tram.ordersandcustomers.customers;

import io.eventuate.tram.consumer.redis.TramConsumerRedisConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Import({TramConsumerRedisConfiguration.class, CustomerCommonConfiguration.class})
@Profile("Redis")
public class CustomerRedisConfiguration {
}
