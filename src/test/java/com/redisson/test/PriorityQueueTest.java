package com.redisson.test;

import java.time.Duration;

import com.redisson.test.assignment.Category;
import com.redisson.test.assignment.PriorityQueue;
import com.redisson.test.assignment.UserOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class PriorityQueueTest extends BaseTest {

    private PriorityQueue priorityQueue;

    @BeforeAll
    public void setUp() {
        RScoredSortedSetReactive<UserOrder> sortedSet =
            client.getScoredSortedSet("user:order:queue", new TypedJsonJacksonCodec(UserOrder.class));
        priorityQueue = new PriorityQueue(sortedSet);
    }
    @Test
    void producer() {
        Flux.interval(Duration.ofSeconds(1))
            .map(l -> l.intValue() * 5)
            .doOnNext(i -> {
                UserOrder order1 = new UserOrder(i + 1, Category.GUEST);
                UserOrder order2 = new UserOrder(i + 2, Category.STD);
                UserOrder order3 = new UserOrder(i + 3, Category.PRIME);
                UserOrder order4 = new UserOrder(i + 4, Category.STD);
                UserOrder order5 = new UserOrder(i + 5, Category.GUEST);
                Mono<Void> then = Flux.just(order1, order2, order3, order4, order5)
                    .flatMap(priorityQueue::add)
                    .then();
                StepVerifier.create(then)
                    .verifyComplete();
            }).subscribe();

        sleep(60);
    }

    @Test
    void consumer() {
        priorityQueue.takeItems()
            .delayElements(Duration.ofSeconds(1))
            .doOnNext(System.out::println)
            .subscribe();

        sleep(600);
    }
}