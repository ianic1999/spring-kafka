# Spring-Kafka demo

This project provides a demo for how to use Kafka in a Spring Boot Application.
Apache Kafka is an open-source distributed streaming platform that is designed to handle large volumes of real-time data streams efficiently. It provides a publish-subscribe messaging system, where producers write messages to topics, and consumers subscribe to those topics to receive and process the messages. Kafka is highly scalable, fault-tolerant, and offers low-latency processing, making it ideal for building real-time data pipelines and streaming applications.

## Install & Run Kafka locally on Windows

1. Download Apache Kafka: https://www.apache.org/dyn/closer.cgi?path=/kafka/3.5.0/kafka_2.13-3.5.0.tgz
2. Extract files locally on your computer
3. Navigate to the extracted folder and open Command Prompt
4. Run Zookeeper: `.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties`
5. Run Apache Kafka: `.\bin\windows\kafka-server-start.bat .\config\server.properties`
   - Kafka Cluster is running on `localhost:9092`

## Kafka Producer

Configuration:

```java
    @Value("${spring.kafka.producer.bootstrap-servers}") //localhost:9092
    private String bootstrapServers;

    @Bean
    public KafkaTemplate<String, String> messagePayloadKafkaTemplate() {
        return new KafkaTemplate<>(messagePayloadProducerFactory());
    }

    @Bean
    public ProducerFactory<String, String> messagePayloadProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new DefaultKafkaProducerFactory<>(configProps);
    }
```

This configuration allows you to write messages to kafka, on a certain topic, as in the example:

```java
    @Value("${kafka.topic.name}") //message.topic
    private String stringTopic;

    public void sendAsString(String payload) {
        log.info("Sending message as STRING with payload: {}", payload);
        stringKafkaTemplate.send(stringTopic, payload);
    }
```

Application listen for messages at `/api/messages/send` endpoint as REST requests and send them forward to Kafka

## Kafka Consumer

The `kafka-consumer` module allows you to consume messages from a Kafka cluster.

### Configuration

1. Open the `application.properties` file in the `kafka-consumer` module.
  ```
  spring.kafka.consumer.bootstrapServers=127.0.0.1:9092
  spring.kafka.consumer.topic=message.topic
  spring.kafka.consumer.groupId=group.1
  spring.kafka.consumer.clientId=client.1
  spring.kafka.consumer.autoCommit=false
  ```
2. We use `@ConfigurationProperties` for binding properties to a Java class.
  ```java
  @Component
  @ConfigurationProperties(prefix = "spring.kafka.consumer")
  @Getter
  @Setter
  class KafkaConsumerProperties {
      private String bootstrapServers;
      private String topic;
      private String groupId;
      private String clientId;
      private boolean autoCommit;
  }
  ```
3. Consumer configuration class:
   ```java
    private final KafkaConsumerProperties props;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(receiverOptions());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> receiverOptions() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getBootstrapServers());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, props.getGroupId());
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, props.getClientId());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, props.isAutoCommit());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);


        return new DefaultKafkaConsumerFactory<>(properties, new StringDeserializer(), new StringDeserializer());
    }
   ```

## Message consume

As we've configured our Kafka consumer, we can now listen for messages at a certain topic:

```java
@KafkaListener(topics = "${spring.kafka.consumer.topic}") //message.topic
    public void listen(@Payload String message, Acknowledgment acknowledgment) {
        log.info("Received message: {}", message);
        acknowledgment.acknowledge();
    }
```

After starting the consumer, it starts to consume messages:

![image](https://github.com/ianic1999/spring-kafka/assets/52444793/53b7f780-b011-4b38-aad7-53a568ed307a)

## Conclusion

The demo applications provides an easy way to write messages to a Kafka cluster and consume them within your Spring application. By following the configuration and usage instructions provided in this documentation, you can effectively leverage Kafka's real-time streaming capabilities in your project.
