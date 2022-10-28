package ru.job4j.synch;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SimpleBlockingQueueTest {

    @Test
    public void whenProducerTwiAddAndConsumerOnePoll() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);
        Thread consumer = new Thread(() -> {
            try {
                queue.poll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread producer = new Thread(() -> {
            try {
                queue.offer(1);
                queue.offer(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        consumer.start();
        producer.start();
        producer.join();
        consumer.join();
        assertThat(queue.poll(), is(2));
    }

    @Test
    public void whenQueueOfTwoElementsUseLimit() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);
        Thread consumer = new Thread(() -> {
            try {
                queue.poll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread producer = new Thread(() -> {
            try {
                queue.offer(1);
                queue.offer(2);
                queue.offer(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        consumer.start();
        producer.start();
        producer.join();
        consumer.join();
        assertThat(queue.poll(), is(2));
        assertThat(queue.poll(), is(3));
    }

    @Test
    public void whenFitchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue =  new SimpleBlockingQueue<>();
        Thread producer = new Thread(
                () -> {
                    IntStream.range(0, 5).forEach(
                            value -> {
                                try {
                                    queue.offer(value);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
                }
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                            try {
                                buffer.add(queue.poll());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer, is(Arrays.asList(0, 1, 2, 3, 4)));
    }
}