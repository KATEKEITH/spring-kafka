package org.example.kafkaproject.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    private final Producer<String, String> producer;
    
    private final String topic;
    
    private final AtomicLong messageCount = new AtomicLong(0);

    
    public MessageProducer() {
        Properties props = KafkaProducerConfig.getProducerConfig();
        this.producer = new KafkaProducer<>(props);
        this.topic = KafkaProducerConfig.getTopic();
    }

    public void sendMessage(Message message) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(
                    topic,
                    message.getId(),
                    message.toString()
            );

            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    logger.error("메시지 전송 실패: " + exception.getMessage());
                } else {
                    long count = messageCount.incrementAndGet();
                    logger.info(String.format(
                            "메시지 전송 성공 - Topic: %s, Partition: %d, Offset: %d, 총 전송 수: %d",
                            metadata.topic(), metadata.partition(), metadata.offset(), count
                    ));
                }
            });
        } catch (Exception e) {
            logger.error("메시지 전송 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("메시지 전송 실패", e);
        }
    }

    public void close() {
        producer.flush();
        producer.close();
        logger.info("Producer 종료. 총 전송된 메시지 수: " + messageCount.get());
    }

    // 테스트용 메시지 생성 메서드
    public Message createTestMessage(int sizeInKB) {

        StringBuilder content = new StringBuilder();
        
        // sizeInKB만큼의 랜덤 문자열 생성
        for (int i = 0; i < sizeInKB * 1024; i++) {
            content.append((char) ('A' + Math.random() * 26));
        }
        
        return new Message(content.toString());
    }

    public static void main(String[] args) {
        MessageProducer producer = new MessageProducer();
        try {
            // 20000개의 메시지를 1초 동안 전송하기 위한 설정
            int totalMessages = 20000;
            long startTime = System.currentTimeMillis();

            ExecutorService executorService = Executors.newFixedThreadPool(200);
            List<Future<?>> futures = new ArrayList<>();

            // 여러 스레드로 메시지 생성 및 전송
            for (int i = 0; i < totalMessages; i++) {
                final int messageIndex = i;
                futures.add(executorService.submit(() -> {
                    try {
                        Message message = producer.createTestMessage(500); // 500KB 메시지
                        producer.sendMessage(message);
                    } catch (Exception e) {
                        logger.error("메시지 전송 실패: " + e.getMessage());
                    }
                }));
            }

            // 모든 메시지 전송 완료 대기
            for (Future<?> future : futures) {
                future.get();
            }

            long endTime = System.currentTimeMillis();
            logger.info("총 소요 시간: {}ms", endTime - startTime);
            logger.info("초당 메시지 처리량: {}", totalMessages / ((endTime - startTime) / 1000.0));

            executorService.shutdown();
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        } finally {
            producer.close();
        }
    }

}
