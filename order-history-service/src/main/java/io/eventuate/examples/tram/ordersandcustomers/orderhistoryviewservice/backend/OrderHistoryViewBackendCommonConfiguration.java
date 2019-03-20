package io.eventuate.examples.tram.ordersandcustomers.orderhistoryviewservice.backend;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({OrderHistoryViewRedisConfiguration.class})
public class OrderHistoryViewBackendCommonConfiguration {

  @Bean
  public OrderHistoryEventConsumer orderEventConsumer() {
    return new OrderHistoryEventConsumer();
  }

  @Bean("orderHistoryDomainEventDispatcher")
  public DomainEventDispatcher orderHistoryDomainEventDispatcher(OrderHistoryEventConsumer orderHistoryEventConsumer,
                                                                 MessageConsumer messageConsumer) {

    return new DomainEventDispatcher("orderHistoryServiceEvents",
            orderHistoryEventConsumer.domainEventHandlers(), messageConsumer);
  }

  @Bean
  public CustomerHistoryEventConsumer customerHistoryEventConsumer() {
    return new CustomerHistoryEventConsumer();
  }

  @Bean("customerHistoryDomainEventDispatcher")
  public DomainEventDispatcher customerHistoryDomainEventDispatcher(CustomerHistoryEventConsumer customerHistoryEventConsumer,
                                                                    MessageConsumer messageConsumer) {

    return new DomainEventDispatcher("customerHistoryServiceEvents",
            customerHistoryEventConsumer.domainEventHandlers(), messageConsumer);
  }
}
