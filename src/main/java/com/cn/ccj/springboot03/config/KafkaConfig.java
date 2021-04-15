package com.cn.ccj.springboot03.config;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author jiangcongcong
 * @date 2021/4/15 16:07
 * kafka官网教程：https://www.orchome.com/303、https://www.orchome.com/451
 */
@Configuration
public class KafkaConfig {

    //kafka生产者
    @Bean
    public KafkaProducer kafkaProducer(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); //kafka集群地址及端口
        props.put("acks", "all"); //ack是判别请求是否为完整的条件（就是是判断是不是成功发送了）。我们指定了“all”将会阻塞消息，这种设置性能最低，但是是最可靠的。
        props.put("retries", 0); //retries，如果请求失败，生产者会自动重试，我们指定是0次，如果启用重试，则会有重复消息的可能性。
        props.put("batch.size", 16384); //缓存的大小是通过 batch.size 配置指定的。值较大的话将会产生更大的批。并需要更多的内存（因为每个“活跃”的分区都有1个缓冲区）。
        props.put("linger.ms", 1); //默认缓冲可立即发送，即便缓冲空间还没有满，但是，如果你想减少请求的数量，可以设置linger.ms大于0。这将指示生产者发送请求之前等待一段时间，希望更多的消息填补到未满的批中
        props.put("buffer.memory", 33554432); //buffer.memory 控制生产者可用的缓存总量，如果消息发送速度比其传输到服务器的快，将会耗尽这个缓存空间。当缓存空间耗尽，其他发送调用将被阻塞
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); //将用户提供的key对象ProducerRecord转换成字节
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); //将用户提供的value对象ProducerRecord转换成字节
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        return producer;
    }

    //kafka消费者
    @Bean
    public KafkaConsumer kafkaConsumer(){
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        return consumer;
    }

}
