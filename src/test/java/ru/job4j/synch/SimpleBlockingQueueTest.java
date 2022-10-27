package ru.job4j.synch;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SimpleBlockingQueueTest {

    @Test
    public void whenProducerTwiAddAndConsumerOnePoll() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);
        Thread consumer =  new Thread(() -> {
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
        Thread consumer =  new Thread(() -> {
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
}