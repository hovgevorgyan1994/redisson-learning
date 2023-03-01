package com.redisson.test;

import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class SortedSetTest extends BaseTest {

    @Test
    void sortedSet() {
        var sortedSet = client.getScoredSortedSet("student:score", StringCodec.INSTANCE);
        Mono<Void> then = sortedSet.addScore("sam", 200.25)
            .then(sortedSet.add(22.55, "mike"))
            .then(sortedSet.addScore("jake", 7))
            .then();

        StepVerifier.create(then)
            .verifyComplete();

        sortedSet.entryRangeReversed(0,1)
            .flatMapIterable(Function.identity())
            .map(se -> se.getScore() + ":" + se.getValue())
            .doOnNext(System.out::println)
            .subscribe();

        sleep(1);
    }
}
