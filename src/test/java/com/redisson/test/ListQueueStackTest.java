package com.redisson.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.IntegerCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ListQueueStackTest extends BaseTest {

    @BeforeEach
    void tearDown() {
        client.getList("numbers").delete().subscribe();
    }

    @Test
    void listTest() {
        RListReactive<Integer> numbers = client.getList("numbers", IntegerCodec.INSTANCE);
        Mono<Void> then = Flux.range(1, 10)
            .doOnNext(System.out::println)
            .flatMap(numbers::add)
            .then();

        StepVerifier.create(then)
            .verifyComplete();
        StepVerifier.create(numbers.size())
            .expectNext(10)
            .verifyComplete();
    }
}
