package io.eventuate.examples.tram.ordersandcustomers.orderhistoryviewservice.backend;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.PartialUpdate;
import org.springframework.data.redis.core.RedisKeyValueTemplate;

public class CustomerViewRepositoryCustomImpl implements CustomerViewRepositoryCustom {

  @Autowired
  private RedisKeyValueTemplate redisKeyValueTemplate;

  @Override
  public void createOrUpdateCustomerView(CustomerView customerView) {
    redisKeyValueTemplate.update(new PartialUpdate<>(customerView.getId(), customerView));
  }
}
