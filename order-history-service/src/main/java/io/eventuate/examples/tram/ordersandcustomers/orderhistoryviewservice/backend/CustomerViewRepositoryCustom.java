package io.eventuate.examples.tram.ordersandcustomers.orderhistoryviewservice.backend;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;

public interface CustomerViewRepositoryCustom {
  void createOrUpdateCustomerView(CustomerView customerView);
}
