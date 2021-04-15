package com.cn.ccj.springboot03;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Arrays;

/**
 * @author jiangcongcong
 * @date 2021/4/15 16:28
 * kafka消费者测试
 */
@SpringBootTest
public class KafkaConsumeTest {

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Test
    public void test2() {
        kafkaConsumer.subscribe(Arrays.asList("my-topic"));//kafka消费话题，可以有多个
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }

}
