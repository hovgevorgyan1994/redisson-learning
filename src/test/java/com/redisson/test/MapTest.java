package com.redisson.test;

import java.util.List;
import java.util.Map;

import com.redisson.test.model.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MapTest extends BaseTest {

    @Test
    void mapTest() {
        RMapReactive<String, String> map = client.getMap("user:1", StringCodec.INSTANCE);
        Mono<String> name = map.put("name", "sam");
        Mono<String> age = map.put("age", "10");
        Mono<String> city = map.put("city", "gyumri");

        StepVerifier.create(name.concatWith(age).concatWith(city).then())
            .verifyComplete();
    }

    @Test
    void mapTest1() {
        RMapReactive<String, String> map = client.getMap("user:2", StringCodec.INSTANCE);
        Map<String, String> name1 = Map.of("name", "poxos", "age", "30");
        Mono<Void> voidMono = map.putAll(name1);

        StepVerifier.create(voidMono)
            .verifyComplete();
    }

    @Test
    void mapTest2() {
        RMapReactive<Integer, Student> map =
            client.getMap("users", new TypedJsonJacksonCodec(Integer.class, Student.class));

        Student student1 = new Student("poxos", 25, "gyumri", List.of(1,2,300));
        Student student2 = new Student("petros", 50, "gyumri", List.of(1,2,3));

        Mono<Student> mono1 = map.put(1, student1);
        Mono<Student> mono2 = map.put(2, student2);

        StepVerifier.create(mono1.concatWith(mono2).then())
            .verifyComplete();
    }
}

