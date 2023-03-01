package com.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RPatternTopicReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.client.codec.StringCodec;

public class PubSubTest extends BaseTest {

    @Test
    void subscriber1() {
        RTopicReactive topic = client.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
            .doOnError(System.out::println)
            .doOnNext(System.out::println)
            .subscribe();

        sleep(600);
    }

    @Test
    void subscriber2() {
        RPatternTopicReactive patternTopic = client.getPatternTopic("slack*", StringCodec.INSTANCE);
        patternTopic.addListener(String.class, (pattern, topic, message) -> {
            System.out.println(pattern + " : " + topic + " : " + message);
        }).subscribe();

        sleep(600);
    }
}
