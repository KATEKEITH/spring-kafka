package org.example.kafkaproject.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerConfig {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    private static final String TOPIC = "message-topic";

    public static Properties getProducerConfig() {
        
        Properties props = new Properties();

        // 필수 설정
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 성능 최적화 설정
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");  // 16KB
        props.put(ProducerConfig.LINGER_MS_CONFIG, "10");      // 10ms
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432"); // 32MB
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

        // 안정성 설정
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.RETRIES_CONFIG, "3");
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "1572864"); // 1.5MB

        return props;
    }

    public static String getTopic() {
        return TOPIC;
    }

}
