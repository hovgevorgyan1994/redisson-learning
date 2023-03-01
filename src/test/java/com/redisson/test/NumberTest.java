package com.redisson.test;

import static java.time.Duration.ofSeconds;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLongReactive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class NumberTest extends BaseTest {

    @Test
    void keyValueObjectTest() {
        RAtomicLongReactive visit = client.getAtomicLong("visit");
        Mono<Void> mono = Flux.range(1, 15)
            .delayElements(ofSeconds(1))
            .flatMap(i -> visit.incrementAndGet())
            .doOnNext(System.out::println)
            .then();

        StepVerifier.create(mono)
            .verifyComplete();
    }
}
