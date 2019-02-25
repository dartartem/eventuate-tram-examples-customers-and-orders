package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;
import org.springframework.data.repository.CrudRepository;

public interface OrderViewRepository extends CrudRepository<OrderView, Long> {
}
