package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.PartialUpdate;
import org.springframework.data.redis.core.RedisKeyValueTemplate;

public class OrderViewRepositoryCustomImpl implements OrderViewRepositoryCustom {

  @Autowired
  private RedisKeyValueTemplate redisKeyValueTemplate;

  @Override
  public void createOrUpdateOrderView(OrderView orderView) {
    redisKeyValueTemplate.update(new PartialUpdate<>(orderView.getId(), orderView));
  }
}
