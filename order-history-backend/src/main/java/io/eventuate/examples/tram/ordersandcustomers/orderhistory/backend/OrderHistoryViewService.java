package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderInfo;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;

public class OrderHistoryViewService {

  private CustomerViewRepository customerViewRepository;
  private OrderViewRepository orderViewRepository;

  public OrderHistoryViewService(CustomerViewRepository customerViewRepository,
                                 OrderViewRepository orderViewRepository) {

    this.customerViewRepository = customerViewRepository;
    this.orderViewRepository = orderViewRepository;
  }

  public void createCustomer(Long customerId, String customerName, Money creditLimit) {
    customerViewRepository.createOrUpdateCustomerView(new CustomerView(customerId, customerName, creditLimit));
  }

  public void addOrder(Long customerId, Long orderId, Money orderTotal) {
    CustomerView customerView = new CustomerView(customerId);
    customerView.addOrder(new OrderInfo(orderId, orderTotal));
    customerViewRepository.createOrUpdateCustomerView(customerView);

    orderViewRepository.createOrUpdateOrderView(new OrderView(orderId, orderTotal));
  }

  public void approveOrder(Long customerId, Long orderId) {
    changeOrderState(customerId, orderId, OrderState.APPROVED);
  }

  public void rejectOrder(Long customerId, Long orderId) {
    changeOrderState(customerId, orderId, OrderState.REJECTED);
  }

  private void changeOrderState(Long customerId, Long orderId, OrderState state) {
    CustomerView customerView = new CustomerView(customerId);
    customerView.addOrder(new OrderInfo(orderId, state));
    customerViewRepository.createOrUpdateCustomerView(customerView);

    OrderView orderView = new OrderView(orderId);
    orderView.setState(state);
    orderViewRepository.createOrUpdateOrderView(orderView);
  }
}
