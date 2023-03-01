package com.redisson.test;

import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBlockingDequeReactive;
import org.redisson.client.codec.IntegerCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MessageQueueTest extends BaseTest {
    private RBlockingDequeReactive<Integer> msgQueue;

    @BeforeAll
    void setUpQueue() {
        msgQueue = client.getBlockingDeque("message-queue", IntegerCodec.INSTANCE);
    }

    @Test
    void consumer1() {
        msgQueue.takeElements()
            .doOnNext(i -> System.out.println("Consumer 1 received: " + i))
            .doOnError(System.out::println)
            .subscribe();
        sleep(600);
    }

    @Test
    void consumer2() {
        msgQueue.takeElements()
            .doOnNext(i -> System.out.println("Consumer 2 received: " + i))
            .doOnError(System.out::println)
            .subscribe();
        sleep(600);
    }

    @Test
    void producer(){
        Mono<Void> mono = Flux.range(1, 100)
            .delayElements(Duration.ofMillis(500))
            .doOnNext(i -> System.out.println("Going to add " + i + " to msg queue"))
            .flatMap(msgQueue::add)
            .then();

        StepVerifier.create(mono)
            .verifyComplete();

    }
}
