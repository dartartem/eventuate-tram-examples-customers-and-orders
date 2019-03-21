package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.persistence;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;

public interface OrderViewRepositoryCustom {
  void createOrUpdateOrderView(OrderView orderView);
}
