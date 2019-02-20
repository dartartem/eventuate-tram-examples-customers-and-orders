package net.chrisrichardson.eventstore.examples.customersandorders.views.orderhistory;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.OrderHistoryViewBackendKafkaConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.OrderHistoryViewBackendRedisConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({OrderHistoryViewBackendKafkaConfiguration.class, OrderHistoryViewBackendRedisConfiguration.class})
public class OrderHistoryViewServiceTestConfiguration {
}
