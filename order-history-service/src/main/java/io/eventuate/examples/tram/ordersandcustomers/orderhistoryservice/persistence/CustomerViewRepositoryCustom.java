package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.persistence;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;

public interface CustomerViewRepositoryCustom {
  void createOrUpdateCustomerView(CustomerView customerView);
}
