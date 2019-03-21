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
  public OrderHistoryServiceEventSubscriber orderEventConsumer() {
    return new OrderHistoryServiceEventSubscriber();
  }

  @Bean
  public DomainEventDispatcher orderHistoryServiceEventDispatcher(OrderHistoryServiceEventSubscriber orderHistoryServiceEventSubscriber,
                                                                       MessageConsumer messageConsumer) {

    return new DomainEventDispatcher("orderHistoryServiceEvents",
            orderHistoryServiceEventSubscriber.domainEventHandlers(), messageConsumer);
  }

}
