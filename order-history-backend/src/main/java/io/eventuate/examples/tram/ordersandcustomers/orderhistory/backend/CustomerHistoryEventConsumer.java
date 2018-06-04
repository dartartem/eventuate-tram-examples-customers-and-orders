package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import com.google.common.collect.ImmutableList;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerSnapshotEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerSnapshotStartingOffsetEvent;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import io.eventuate.local.java.kafka.consumer.EventuateKafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.UUID;


public class CustomerHistoryEventConsumer {

  @Autowired
  private OrderHistoryViewService orderHistoryViewService;

  @Value("${eventuatelocal.kafka.bootstrap.servers}")
  private String kafkaBootstrapServers;

//  @Autowired
//  private EventuateKafkaConsumer eventuateKafkaConsumer;
//  public DomainEventHandlers domainEventHandlers() {
//    return DomainEventHandlersBuilder
//            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
//            .onEvent(CustomerCreatedEvent.class, this::customerCreatedEventHandler)
//            .build();
//  }
//
//  private void customerCreatedEventHandler(DomainEventEnvelope<CustomerCreatedEvent> domainEventEnvelope) {
//    CustomerCreatedEvent customerCreatedEvent = domainEventEnvelope.getEvent();
//    orderHistoryViewService.createCustomer(Long.parseLong(domainEventEnvelope.getAggregateId()),
//            customerCreatedEvent.getName(), customerCreatedEvent.getCreditLimit());
//  }

  @PostConstruct
  public void init() {
    EventuateKafkaConsumer offsetConsumer = new EventuateKafkaConsumer(UUID.randomUUID().toString(), (offsetRecord, offsetCallback) -> {
      CustomerSnapshotStartingOffsetEvent snapshotStartingOffsetEvent = JSonMapper.fromJson(offsetRecord.value(),
              CustomerSnapshotStartingOffsetEvent.class);

      EventuateKafkaConsumer snapshotConsumer = new EventuateKafkaConsumer(UUID.randomUUID().toString(),
              (snapshotRecord, snapshotCallback) -> {
                CustomerSnapshotEvent customerSnapshotEvent = JSonMapper.fromJson(snapshotRecord.value(), CustomerSnapshotEvent.class);

                orderHistoryViewService.createCustomer(customerSnapshotEvent.getId(),
                        customerSnapshotEvent.getName(),
                        customerSnapshotEvent.getCreditLimit());

                offsetCallback.accept(null, null);
              },
              ImmutableList.of("CustomerSnapshot"),
              kafkaBootstrapServers);

      //TODO: fix consumer fix (error: No current assignment for partition CustomerSnapshot-1)
//      snapshotConsumer.setTopicPartitionOffsets(ImmutableMap.of(new TopicPartition(snapshotStartingOffsetEvent.getTopic(),
//              snapshotStartingOffsetEvent.getPartition()), snapshotStartingOffsetEvent.getOffset()));

      snapshotConsumer.start();

      offsetCallback.accept(null, null);
    }, ImmutableList.of("CustomerSnapshotStartingOffset"), kafkaBootstrapServers);

    offsetConsumer.start();
  }
}
