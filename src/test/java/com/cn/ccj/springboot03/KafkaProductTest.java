package com.cn.ccj.springboot03;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Arrays;

/**
 * @author jiangcongcong
 * @date 2021/4/15 16:13
 * kafka生产者测试
 */

@SpringBootTest
public class KafkaProductTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    public void test1() {
        for (int i = 0; i < 10; i++) {
            //send()方法是异步的，添加消息到缓冲区等待发送，并立即返回。生产者将单个的消息批量在一起发送来提高效率。
            kafkaProducer.send(new ProducerRecord<String, String>("my-topic", Integer.toString(i), Integer.toString(i)));
        }
        kafkaProducer.close();
    }


}
