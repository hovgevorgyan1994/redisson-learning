package com.redisson.test;

import static java.util.concurrent.TimeUnit.SECONDS;

import org.junit.jupiter.api.Test;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class EventListenerTest extends BaseTest {

    @Test
    void expiredEventTest() {
        var bucket = client.getBucket("user:1:name", StringCodec.INSTANCE);
        var set = bucket.set("sam", 10, SECONDS);
        var get = bucket.get()
            .doOnNext(System.out::println)
            .then();
        Mono<Void> event = bucket.addListener((ExpiredObjectListener) s -> {
            System.out.println("Expired: " + s);
        }).then();

        StepVerifier.create(set.concatWith(get).concatWith(event))
            .verifyComplete();

        //extend
        sleep(13);
    }

    @Test
    void deletedEventListener() {
        var bucket = client.getBucket("user:1:name", StringCodec.INSTANCE);
        var set = bucket.set("sam");
        var get = bucket.get()
            .doOnNext(System.out::println)
            .then();

        Mono<Void> event = bucket.addListener((DeletedObjectListener) s -> System.out.println("Deleted: " + s)).then();

        StepVerifier.create(set.concatWith(get).concatWith(event))
            .verifyComplete();

        //extend
        sleep(60);
    }
}
