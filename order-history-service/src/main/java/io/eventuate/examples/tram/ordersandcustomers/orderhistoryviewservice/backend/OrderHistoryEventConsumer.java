package io.eventuate.examples.tram.ordersandcustomers.orderhistoryviewservice.backend;

import io.eventuate.examples.tram.ordersandcustomers.orderservice.domain.events.OrderApprovedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orderservice.domain.events.OrderCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orderservice.domain.events.OrderRejectedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderHistoryEventConsumer {

  @Autowired
  private OrderHistoryViewService orderHistoryViewService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order")
            .onEvent(OrderCreatedEvent.class, this::orderCreatedEventHandler)
            .onEvent(OrderApprovedEvent.class, this::orderApprovedEventHandler)
            .onEvent(OrderRejectedEvent.class, this::orderRejectedEventHandler)
            .build();
  }

  private void orderCreatedEventHandler(DomainEventEnvelope<OrderCreatedEvent> domainEventEnvelope) {
    OrderCreatedEvent orderCreatedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.addOrder(orderCreatedEvent.getOrderDetails().getCustomerId(),
            Long.parseLong(domainEventEnvelope.getAggregateId()), orderCreatedEvent.getOrderDetails().getOrderTotal());
  }

  private void orderApprovedEventHandler(DomainEventEnvelope<OrderApprovedEvent> domainEventEnvelope) {
    OrderApprovedEvent orderApprovedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.approveOrder(orderApprovedEvent.getOrderDetails().getCustomerId(),
            Long.parseLong(domainEventEnvelope.getAggregateId()));
  }

  private void orderRejectedEventHandler(DomainEventEnvelope<OrderRejectedEvent> domainEventEnvelope) {
    OrderRejectedEvent orderRejectedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.rejectOrder(orderRejectedEvent.getOrderDetails().getCustomerId(),
            Long.parseLong(domainEventEnvelope.getAggregateId()));
  }
}
