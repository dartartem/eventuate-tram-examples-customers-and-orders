package io.eventuate.examples.tram.ordersandcustomers.endtoendtests;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerRequest;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerResponse;
import io.eventuate.examples.tram.ordersandcustomers.orderservice.domain.events.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderRequest;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderResponse;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.GetOrderResponse;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;
import io.eventuate.util.test.async.Eventually;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CustomersAndOrdersEndToEndTestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CustomersAndOrdersEndToEndTest{

  @Value("#{systemEnvironment['DOCKER_HOST_IP']}")
  private String hostName = "localhost";

  private String baseUrlCustomers(String path) {
    return "http://"+hostName+":8081/" + path;
  }

  private String baseUrlOrders(String path) {
    return "http://"+hostName+":8082/" + path;
  }

  private String baseUrlOrderHistory(String path) {
    return "http://"+hostName+":8083/" + path;
  }

  @Autowired
  RestTemplate restTemplate;

  @Test
  public void shouldApprove() {
    Long customerId = createCustomer("Fred", new Money("15.00"));
    Long orderId = createOrder(customerId, new Money("12.34"));
    assertOrderState(orderId, OrderState.APPROVED);
  }

  @Test
  public void shouldReject() {
    Long customerId = createCustomer("Fred", new Money("15.00"));
    Long orderId = createOrder(customerId, new Money("123.34"));
    assertOrderState(orderId, OrderState.REJECTED);
  }

  @Test
  public void shouldRejectForNonExistentCustomerId() {
    Long customerId = System.nanoTime();
    Long orderId = createOrder(customerId, new Money("123.34"));
    assertOrderState(orderId, OrderState.REJECTED);
  }

  @Test
  public void shouldRejectApproveAndKeepOrdersInHistory() {
    Long customerId = createCustomer("John", new Money("1000"));

    Long order1Id = createOrder(customerId, new Money("100"));
    Long order2Id = createOrder(customerId, new Money("1000"));

    String order1HistoryUrl = baseUrlOrderHistory("orders") + "/" + order1Id;
    String order2HistoryUrl = baseUrlOrderHistory("orders") + "/" + order2Id;
    String customerHistoryUrl = baseUrlOrderHistory("customers") + "/" + customerId;

    Eventually.eventually(() -> {
      ResponseEntity<CustomerView> customerViewResponseEntity = restTemplate.getForEntity(customerHistoryUrl,
              CustomerView.class);

      ResponseEntity<OrderView> orderView1ResponseEntity = restTemplate.getForEntity(order1HistoryUrl,
              OrderView.class);

      ResponseEntity<OrderView>  orderView2ResponseEntity = restTemplate.getForEntity(order2HistoryUrl,
              OrderView.class);

      Assert.assertNotNull(customerViewResponseEntity);
      Assert.assertNotNull(orderView1ResponseEntity);
      Assert.assertNotNull(orderView2ResponseEntity);

      OrderState order1State = customerViewResponseEntity.getBody().getOrders().get(order1Id).getState();
      OrderState order2State = customerViewResponseEntity.getBody().getOrders().get(order2Id).getState();

      Assert.assertEquals(2, customerViewResponseEntity.getBody().getOrders().size());
      assertOrderApprovedOrRejected(order1State);
      assertOrderApprovedOrRejected(order2State);
      Assert.assertNotEquals(order1State, order2State);

      assertOrderApprovedOrRejected(orderView1ResponseEntity.getBody().getState());
      assertOrderApprovedOrRejected(orderView2ResponseEntity.getBody().getState());
      Assert.assertNotEquals(orderView1ResponseEntity.getBody().getState(), orderView2ResponseEntity.getBody().getState());
    });
  }


  private Long createCustomer(String name, Money credit) {
    return restTemplate.postForObject(baseUrlCustomers("customers"),
            new CreateCustomerRequest(name, credit), CreateCustomerResponse.class).getCustomerId();
  }

  private Long createOrder(Long customerId, Money orderTotal) {
    return restTemplate.postForObject(baseUrlOrders("orders"),
            new CreateOrderRequest(customerId, orderTotal), CreateOrderResponse.class).getOrderId();
  }

  private void assertOrderApprovedOrRejected(OrderState orderState) {
    Assert.assertTrue(OrderState.APPROVED == orderState || orderState == OrderState.REJECTED);
  }

  private void assertOrderState(Long id, OrderState expectedState) {
    Eventually.eventually(() -> {
      ResponseEntity<GetOrderResponse> getOrderResponseEntity =
              restTemplate.getForEntity(baseUrlOrders("orders/" + id), GetOrderResponse.class);

      GetOrderResponse order = getOrderResponseEntity.getBody();

      Assert.assertEquals(expectedState, order.getOrderState());
    });
  }
}
