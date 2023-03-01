package com.redisson.test;

import static reactor.test.StepVerifier.create;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RTransactionReactive;
import org.redisson.api.TransactionOptions;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TransactionTest extends BaseTest {
    RBucketReactive<Long> user1Balance;
    RBucketReactive<Long> user2Balance;

    @BeforeAll
    void accountSetUp() {
        user1Balance = client.getBucket("user:1:balance", LongCodec.INSTANCE);
        user2Balance = client.getBucket("user:2:balance", LongCodec.INSTANCE);
        create(user1Balance.set(100L)
                   .then(user2Balance.set(0L))
                   .then())
            .verifyComplete();
    }

    @AfterAll
    void accountBalanceStatus() {
        create(Flux.zip(user1Balance.get(), user2Balance.get())
                   .doOnNext(System.out::println)
                   .then()).verifyComplete();
    }

    @Test
    void noneTransactionTest() {
        transfer(user1Balance, user2Balance, 50)
            .thenReturn(0)
            .map(i -> 5 / i)
            .doOnError(System.out::println)
            .subscribe();

        sleep(1);
    }

    @Test
    void transactionTest() {
        RTransactionReactive transaction = client.createTransaction(TransactionOptions.defaults());
        RBucketReactive<Long> user1 = transaction.getBucket("user:1:balance", LongCodec.INSTANCE);
        RBucketReactive<Long> user2 = transaction.getBucket("user:2:balance", LongCodec.INSTANCE);
        transfer(user1, user2, 50)
            .thenReturn(0)
            .map(i -> 5 / i)
            .then(transaction.commit())
            .doOnError(System.out::println)
            .onErrorResume(ex -> transaction.rollback())
            .subscribe();

        sleep(1);
    }

    private Mono<Void> transfer(RBucketReactive<Long> from, RBucketReactive<Long> to, int amount) {
        return Flux.zip(from.get(), to.get())
            .filter(t -> t.getT1() >= amount)
            .flatMap(t -> from.set(t.getT1() - amount).thenReturn(t))
            .flatMap(t -> to.set(t.getT2() + amount).thenReturn(t))
            .then();

    }
}
