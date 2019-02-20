package io.eventuate.examples.tram.ordersandcustomers.customers;

import io.eventuate.tram.consumer.kafka.TramConsumerKafkaConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Import({TramConsumerKafkaConfiguration.class, CustomerCommonConfiguration.class})
@Profile("!Redis")
public class CustomerKafkaConfiguration {
}
