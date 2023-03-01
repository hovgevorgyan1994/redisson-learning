package com.redisson.test;

import java.util.List;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.redisson.api.RHyperLogLogReactive;
import org.redisson.client.codec.LongCodec;
import reactor.test.StepVerifier;

public class HyperLogLogTest extends BaseTest {

    @Test
    void count() {
        RHyperLogLogReactive<Long> counter = client.getHyperLogLog("user:visits", LongCodec.INSTANCE);
        List<Long> longs = LongStream.rangeClosed(1, 2500000).boxed()
            .toList();

        StepVerifier.create(counter.addAll(longs).then())
            .verifyComplete();

        counter.count().subscribe(System.out::println);

    }
}
