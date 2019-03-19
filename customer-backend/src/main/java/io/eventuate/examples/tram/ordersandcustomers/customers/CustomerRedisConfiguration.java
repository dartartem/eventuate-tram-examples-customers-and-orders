package io.eventuate.examples.tram.ordersandcustomers.customers;

import io.eventuate.tram.consumer.redis.TramConsumerRedisConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TramConsumerRedisConfiguration.class, CustomerCommonConfiguration.class})
public class CustomerRedisConfiguration {
}
