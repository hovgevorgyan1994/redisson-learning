package com.redisson.test;

import static java.util.concurrent.TimeUnit.SECONDS;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class KeyValueTest extends BaseTest {

    @Test
    public void keyValue() {
        RBucketReactive<String> bucket = client.getBucket("user:1:name", StringCodec.INSTANCE);
        var set = bucket.set("sam");
        var get = bucket.get()
            .doOnNext(System.out::println)
            .then();

        StepVerifier.create(set.concatWith(get))
            .verifyComplete();
    }

    @Test
    public void keyValueExpiryTest() {
        RBucketReactive<String> bucket = client.getBucket("user:1:name", StringCodec.INSTANCE);
        var set = bucket.set("sam", 10, SECONDS);
        var get = bucket.get()
            .doOnNext(System.out::println)
            .then();

        StepVerifier.create(set.concatWith(get))
            .verifyComplete();
    }

    @Test
    public void keyValueExtendExpiryTest() {
        RBucketReactive<String> bucket = client.getBucket("user:1:name", StringCodec.INSTANCE);
        var set = bucket.set("sam", 10, SECONDS);
        var get = bucket.get()
            .doOnNext(System.out::println)
            .then();

        StepVerifier.create(set.concatWith(get))
            .verifyComplete();

        //extend
        sleep(5);
        var expire = bucket.expire(60, SECONDS);
        StepVerifier.create(expire)
            .expectNext(true)
            .verifyComplete();

        //access expiration time
        Mono<Void> ttl = bucket.remainTimeToLive()
            .doOnNext(System.out::println)
            .then();

        StepVerifier.create(ttl)
            .verifyComplete();
    }
}
