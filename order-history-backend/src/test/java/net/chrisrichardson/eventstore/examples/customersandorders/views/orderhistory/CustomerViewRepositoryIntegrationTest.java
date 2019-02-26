package net.chrisrichardson.eventstore.examples.customersandorders.views.orderhistory;

import com.google.common.collect.ImmutableMap;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.CustomerViewRepository;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.PartialUpdate;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderHistoryViewServiceTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CustomerViewRepositoryIntegrationTest {

  @Autowired
  private CustomerViewRepository customerViewRepository;

  @Autowired
  private RedisKeyValueTemplate redisKeyValueTemplate;

  @Test
  public void shouldCreateAndFindCustomer() {

    Long customerId = System.nanoTime();
    Money creditLimit = new Money(2000);
    String customerName = "Fred";

    customerViewRepository.save(new CustomerView(customerId, customerName, creditLimit));
    CustomerView customerView = customerViewRepository.findById(customerId).get();

    assertEquals(customerId, customerView.getId());
    assertEquals(customerName, customerView.getName());
    assertEquals(0, customerView.getOrders().size());
    assertEquals(creditLimit, customerView.getCreditLimit());
  }

  @Test
  public void testOrderMerging() {
    Long customerId = System.nanoTime();

    Map<Long, OrderInfo> originalOrders = ImmutableMap.of(10L, new OrderInfo(10L, new Money(10)),
            20L, new OrderInfo(20L, new Money(20)));
    createCustomer(customerId, originalOrders);

    Map<Long, OrderInfo> addedOrders = ImmutableMap.of(30L, new OrderInfo(30L, new Money(30)));
    addOrdersToCustomer(customerId, addedOrders);

    Map<Long, OrderInfo> mergedOrders = new HashMap<>();
    mergedOrders.putAll(originalOrders);
    mergedOrders.putAll(addedOrders);

    assertOrdersAreMerged(customerId, mergedOrders);
  }

  @Test
  public void testOrderCreatedIfDoesNotExistWhenMerging() {
    Long customerId = System.nanoTime();

    Map<Long, OrderInfo> addedOrders = ImmutableMap.of(30L, new OrderInfo(30L, new Money(30)));
    addOrdersToCustomer(customerId, addedOrders);

    assertOrdersAreMerged(customerId, addedOrders);
  }

  private void createCustomer(Long customerId, Map<Long, OrderInfo> orders) {
    CustomerView customerView = new CustomerView();
    customerView.setId(customerId);
    customerView.addOrders(orders);
    customerViewRepository.save(customerView);
  }

  private void addOrdersToCustomer(Long customerId, Map<Long, OrderInfo> orders) {
    CustomerView customerView = new CustomerView();
    customerView.setId(customerId);
    customerView.addOrders(orders);
    redisKeyValueTemplate.update(new PartialUpdate<>(customerId, customerView));
  }

  private void assertOrdersAreMerged(Long customerId, Map<Long, OrderInfo> mergedOrders) {
    CustomerView customerView = customerViewRepository.findById(customerId).get();
    Assert.assertEquals(customerView.getOrders(), mergedOrders);
  }
}
