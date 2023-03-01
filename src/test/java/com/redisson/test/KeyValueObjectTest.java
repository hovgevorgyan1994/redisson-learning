package com.redisson.test;

import java.util.List;

import com.redisson.test.model.Student;
import org.junit.jupiter.api.Test;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.test.StepVerifier;

public class KeyValueObjectTest extends BaseTest {

    @Test
    void keyValueObjectTest() {
        var student = new Student("Poxos", 18, "Gyumri", List.of(1, 2, 3));
        var bucket = client.getBucket("student:1", new TypedJsonJacksonCodec(Student.class));
        var set = bucket.set(student);
        var get = bucket.get()
            .doOnNext(System.out::println)
            .then();
        StepVerifier.create(set.concatWith(get))
            .verifyComplete();
    }
}
