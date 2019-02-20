package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.tram.consumer.kafka.TramConsumerKafkaConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Import({OrderHistoryViewBackendCommonConfiguration.class, TramConsumerKafkaConfiguration.class})
@Profile("!Redis")
public class OrderHistoryViewBackendKafkaConfiguration {
}
