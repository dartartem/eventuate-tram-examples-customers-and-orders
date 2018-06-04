package io.eventuate.examples.tram.ordersandcustomers.customers.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerSnapshotEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerSnapshotStartingOffsetEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerRepository;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import io.eventuate.local.java.kafka.producer.EventuateKafkaProducer;
import io.eventuate.tram.events.ResultWithEvents;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.StreamSupport;

public class CustomerService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private DomainEventPublisher domainEventPublisher;

  @Autowired
  private EventuateKafkaProducer eventuateKafkaProducer;

  public Customer createCustomer(String name, Money creditLimit) {
    ResultWithEvents<Customer> customerWithEvents = Customer.create(name, creditLimit);
    Customer customer = customerRepository.save(customerWithEvents.result);
    domainEventPublisher.publish(Customer.class, customer.getId(), customerWithEvents.events);
    return customer;
  }

  public void exportSnapshots() {
    AtomicReference<CompletableFuture<?>> metadataFuture = new AtomicReference<>();

    StreamSupport
            .stream(customerRepository.findAll().spliterator(), false)
            .forEach(customer -> {
              CustomerSnapshotEvent customerSnapshotEvent = new CustomerSnapshotEvent(customer.getId(), customer.getName(), customer.getCreditLimit());

              CompletableFuture<?> metadata = eventuateKafkaProducer.send("CustomerSnapshot", null, JSonMapper.toJson(customerSnapshotEvent));
              metadataFuture.compareAndSet(null, metadata);
            });

    try {
      RecordMetadata metadata = (RecordMetadata)metadataFuture.get().get();

      eventuateKafkaProducer.send("CustomerSnapshotStartingOffset",
              null,
              JSonMapper.toJson(new CustomerSnapshotStartingOffsetEvent(metadata.topic(),
                      metadata.partition(),
                      metadata.offset())));

    } catch (InterruptedException | ExecutionException e) {
      logger.error(e.getMessage(), e);
      //TODO: report error to client
    }
  }
}
