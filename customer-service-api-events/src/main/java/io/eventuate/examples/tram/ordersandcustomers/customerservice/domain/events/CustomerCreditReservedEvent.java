package io.eventuate.examples.tram.ordersandcustomers.customerservice.domain.events;

import io.eventuate.tram.events.common.DomainEvent;

public class CustomerCreditReservedEvent extends AbstractCustomerOrderEvent {

  public CustomerCreditReservedEvent() {
  }

  public CustomerCreditReservedEvent(Long orderId) {
    super(orderId);
  }
}
