package org.wso2.am.analytics.publisher.util;


import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * kafka生产者.
 */
public class KafkaMqProducer {

  private final static String BOOTSTRAP_SERVER = ConfigFactory.getInstance().getStrPropertyValue("kafka.host");
  private static final Logger logger = LogManager.getLogger(KafkaMqProducer.class);
  private static KafkaProducer<String, String> producer;
  private static ExecutorService executorService = Executors.newFixedThreadPool(4);

  private static KafkaProducer<String, String> getProducer() {
    if (producer == null) {
      //reset thread context
      resetThreadContext();
      // create the producer
      producer = new KafkaProducer<String, String>(getProperties());
    }
    return producer;
  }

  public static void publishEvent(String topic, String value) {
    executorService.execute(() -> {
      try {
        // create a producer record
        ProducerRecord<String, String> eventRecord =
            new ProducerRecord<String, String>(topic, value);

        // send data - asynchronous
        getProducer().send(eventRecord, new Callback() {
          @Override
          public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
              e.printStackTrace();
            }
          }
        });

      } catch (Exception ex) {
        logger.error("kafka.error", ex);
      }
    });
  }

  private static void resetThreadContext() {
    Thread.currentThread().setContextClassLoader(null);
  }

  public static Properties getProperties() {
    Properties properties = new Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
    return properties;
  }


}
