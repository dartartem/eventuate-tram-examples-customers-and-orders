package net.chrisrichardson.eventstore.examples.customersandorders.views.orderhistory;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.CustomerViewRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderHistoryViewServiceTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CustomerViewRepositoryIntegrationTest {

  @Autowired
  private CustomerViewRepository customerViewRepository;

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
}
