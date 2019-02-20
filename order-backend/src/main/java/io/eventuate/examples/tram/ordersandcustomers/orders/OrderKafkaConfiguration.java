package io.eventuate.examples.tram.ordersandcustomers.orders;

import io.eventuate.tram.consumer.kafka.TramConsumerKafkaConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Import({TramConsumerKafkaConfiguration.class, OrderCommonConfiguration.class})
@Profile("!Redis")
public class OrderKafkaConfiguration {
}
