package ru.job4j.buffer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class CASCountTest {

    @Test
    public void whenJoin() throws InterruptedException {
        CASCount count = new CASCount();
        Thread first = new Thread(() -> {
            count.increment();
            count.increment();
            count.increment();
        });
        Thread second = new Thread(count::increment);
        first.start();
        second.start();
        first.join();
        second.join();
        assertThat(count.get()).isEqualTo(4);
    }

    @Test
    public void whenNoJoin() {
        CASCount count = new CASCount();
        Thread first = new Thread(() -> {
            count.increment();
            count.increment();
            count.increment();
        });
        Thread second = new Thread(count::increment);
        first.start();
        second.start();
        assertThat(count.get()).isEqualTo(0);
    }

    @Test
    public void whenNoJoinOnSecond() throws InterruptedException {
        CASCount count = new CASCount();
        Thread first = new Thread(() -> {
            count.increment();
            count.increment();
            count.increment();
        });
        Thread second = new Thread(count::increment);
        first.start();
        first.join();
        second.start();
        assertThat(count.get()).isEqualTo(3);
    }
}