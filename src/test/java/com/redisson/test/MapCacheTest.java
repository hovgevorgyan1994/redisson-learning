package com.redisson.test;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.List;

import com.redisson.test.model.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MapCacheTest extends BaseTest {

    @Test
    void mapCacheTest() {
        RMapCacheReactive<Integer, Student> mapCache =
            client.getMapCache("users:cache", new TypedJsonJacksonCodec(Integer.class, Student.class));

        Student student1 = new Student("poxos", 25, "gyumri", List.of(1, 2, 300));
        Student student2 = new Student("petros", 50, "gyumri", List.of(1, 2, 3));

        Mono<Student> mono1 = mapCache.put(1, student1, 5, SECONDS);
        Mono<Student> mono2 = mapCache.put(2, student2, 10, SECONDS);

        StepVerifier.create(mono1.then(mono2).then())
            .verifyComplete();

        sleep(3);

        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();

        sleep(3);
        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();
    }
}
