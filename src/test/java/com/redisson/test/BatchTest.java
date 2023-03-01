package com.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RSetReactive;
import org.redisson.client.codec.LongCodec;
import reactor.test.StepVerifier;

public class BatchTest extends BaseTest {

    @Test
    void test() {
        RBatchReactive batch = client.createBatch(BatchOptions.defaults());
        RListReactive<Long> list = batch.getList("numbers-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = batch.getSet("numbers-set", LongCodec.INSTANCE);
        for (long i = 0; i < 500000; i++) {
            list.add(i);
            set.add(i);
        }
        StepVerifier.create(batch.execute().then())
            .verifyComplete();
    }
}