package net.chrisrichardson.eventstore.examples.customersandorders.orderhistoryservice.backend;

import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.persistence.CustomerViewRepository;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.persistence.OrderHistoryServicePersistenceConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.persistence.OrderViewRepository;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.service.OrderHistoryViewService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import(OrderHistoryServicePersistenceConfiguration.class)
public class OrderHistoryViewServiceTestConfiguration {
  @Bean
  public OrderHistoryViewService orderHistoryViewService(CustomerViewRepository customerViewRepository, OrderViewRepository orderViewRepository) {
    return new OrderHistoryViewService(customerViewRepository, orderViewRepository);
  }
}
