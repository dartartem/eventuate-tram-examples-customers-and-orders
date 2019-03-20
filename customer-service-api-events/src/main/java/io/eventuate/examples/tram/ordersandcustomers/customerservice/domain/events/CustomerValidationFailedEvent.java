package io.eventuate.examples.tram.ordersandcustomers.customerservice.domain.events;

import io.eventuate.tram.events.common.DomainEvent;

public class CustomerValidationFailedEvent extends AbstractCustomerOrderEvent {

  public CustomerValidationFailedEvent(Long orderId) {
    super(orderId);
  }

  public CustomerValidationFailedEvent() {
  }
}
