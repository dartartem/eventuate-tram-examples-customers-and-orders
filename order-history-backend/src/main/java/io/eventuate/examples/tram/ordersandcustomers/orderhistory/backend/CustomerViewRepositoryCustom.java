package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;

public interface CustomerViewRepositoryCustom {
  void createOrUpdateCustomerView(CustomerView customerView);
}
