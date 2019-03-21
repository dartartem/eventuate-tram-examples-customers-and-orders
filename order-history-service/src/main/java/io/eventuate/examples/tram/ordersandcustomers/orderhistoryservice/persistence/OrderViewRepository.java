package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.persistence;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;
import org.springframework.data.repository.CrudRepository;

public interface OrderViewRepository extends CrudRepository<OrderView, Long>, OrderViewRepositoryCustom {
}
