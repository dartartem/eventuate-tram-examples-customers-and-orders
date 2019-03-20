package io.eventuate.examples.tram.ordersandcustomers.orderhistoryviewservice.backend;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;

public interface OrderViewRepositoryCustom {
  void createOrUpdateOrderView(OrderView orderView);
}
