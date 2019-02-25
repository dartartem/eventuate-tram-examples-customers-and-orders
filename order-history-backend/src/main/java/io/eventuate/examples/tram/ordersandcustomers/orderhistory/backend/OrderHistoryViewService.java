package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderInfo;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderHistoryViewService {

  private CustomerViewRepository customerViewRepository;
  private OrderViewRepository orderViewRepository;

  @Autowired
  public OrderHistoryViewService(CustomerViewRepository customerViewRepository, OrderViewRepository orderViewRepository) {
    this.customerViewRepository = customerViewRepository;
    this.orderViewRepository = orderViewRepository;
  }

  public void createCustomer(Long customerId, String customerName, Money creditLimit) {
    customerViewRepository.save(new CustomerView(customerId, customerName, creditLimit));
  }

  public void addOrder(Long customerId, Long orderId, Money orderTotal) {
    CustomerView customerView = findOrCreateCustomerView(customerId);
    customerView.getOrders().put(orderId, new OrderInfo(orderId, orderTotal));
    customerViewRepository.save(customerView);
    orderViewRepository.save(new OrderView(orderId, orderTotal));
  }

  public void approveOrder(Long customerId, Long orderId) {

    CustomerView customerView = findOrCreateCustomerView(customerId);
    customerView.getOrders().get(orderId).approve();
    customerViewRepository.save(customerView);

    OrderView orderView = tryToFindOrderView(orderId);
    orderView.setState(OrderState.APPROVED);
    orderViewRepository.save(orderView);
  }

  public void rejectOrder(Long customerId, Long orderId) {

    CustomerView customerView = findOrCreateCustomerView(customerId);
    customerView.getOrders().get(orderId).reject();
    customerViewRepository.save(customerView);

    OrderView orderView = tryToFindOrderView(orderId);
    orderView.setState(OrderState.REJECTED);
    orderViewRepository.save(orderView);
  }

  private OrderView tryToFindOrderView(Long orderId) {
    return orderViewRepository
            .findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order view does not exist"));
  }

  private CustomerView findOrCreateCustomerView(Long customerId) {
    return customerViewRepository
            .findById(customerId)
            .orElseGet(() -> new CustomerView(customerId));
  }
}
