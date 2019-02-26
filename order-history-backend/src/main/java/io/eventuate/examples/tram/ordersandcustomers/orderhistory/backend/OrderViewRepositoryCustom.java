package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;

public interface OrderViewRepositoryCustom {
  void createOrUpdateOrderView(OrderView orderView);
}
