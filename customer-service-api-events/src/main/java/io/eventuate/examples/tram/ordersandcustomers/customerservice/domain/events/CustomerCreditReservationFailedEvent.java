package io.eventuate.examples.tram.ordersandcustomers.customerservice.domain.events;

import io.eventuate.tram.events.common.DomainEvent;

public class CustomerCreditReservationFailedEvent extends AbstractCustomerOrderEvent {

  public CustomerCreditReservationFailedEvent() {
  }

  public CustomerCreditReservationFailedEvent(Long orderId) {
    super(orderId);
  }


}
