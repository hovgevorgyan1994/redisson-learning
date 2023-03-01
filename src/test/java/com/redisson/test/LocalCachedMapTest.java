package com.redisson.test;

import java.time.Duration;
import java.util.List;

import com.redisson.test.config.RedissonConfig;
import com.redisson.test.model.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.LocalCachedMapOptions.ReconnectionStrategy;
import org.redisson.api.LocalCachedMapOptions.SyncStrategy;
import org.redisson.api.RLocalCachedMap;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;

public class LocalCachedMapTest extends BaseTest {

    private RLocalCachedMap<Integer, Student> studentsMap;

    @BeforeAll
    void setUpClient() {
        var localCachedMapOptions = LocalCachedMapOptions.<Integer, Student>defaults()
            .syncStrategy(SyncStrategy.UPDATE)
            .reconnectionStrategy(ReconnectionStrategy.CLEAR);

        studentsMap = new RedissonConfig().getRedissonClient().getLocalCachedMap("students",
                                                                                 new TypedJsonJacksonCodec(
                                                                                     Integer.class,
                                                                                     Student.class),
                                                                                 localCachedMapOptions);

    }

    @Test
    void appServer1(){
        Student student1 = new Student("poxos", 25, "gyumri", List.of(1, 2, 300));
        Student student2 = new Student("petros", 50, "gyumri", List.of(1, 2, 3));
        studentsMap.put(1,student1);
        studentsMap.put(2,student2);

        Flux.interval(Duration.ofSeconds(1))
            .doOnNext(i -> System.out.println(i + "==> " + studentsMap.get(1)))
            .subscribe();

        sleep(600);
    }

    @Test
    void appServer2(){
        Student student1 = new Student("martiros", 25, "gyumri", List.of(1, 2, 300));
        studentsMap.put(1,student1);
    }
}
