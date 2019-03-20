package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.tram.consumer.common.TramNoopDuplicateMessageDetectorConfiguration;
import io.eventuate.tram.consumer.redis.TramConsumerRedisConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({OrderHistoryViewBackendCommonConfiguration.class, TramConsumerRedisConfiguration.class, TramNoopDuplicateMessageDetectorConfiguration.class})
public class OrderHistoryViewBackendRedisConfiguration {
}
