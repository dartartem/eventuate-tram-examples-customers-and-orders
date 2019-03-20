package io.eventuate.examples.tram.ordersandcustomers.orderhistoryviewservice;

import io.eventuate.examples.tram.ordersandcustomers.orderhistoryviewservice.backend.OrderHistoryViewBackendRedisConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryviewservice.web.OrderHistoryViewWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({OrderHistoryViewWebConfiguration.class,
        OrderHistoryViewBackendRedisConfiguration.class})
public class OrderHistoryServiceMain {

  public static void main(String[] args) {
    SpringApplication.run(OrderHistoryServiceMain.class, args);
  }
}
