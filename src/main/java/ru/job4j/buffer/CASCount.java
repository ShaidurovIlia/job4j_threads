package ru.job4j.buffer;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class CASCount {
    private final AtomicReference<Integer> count = new AtomicReference<>(0);

    public void increment() {
        int numb;
        do {
            numb = count.get();
        } while (!count.compareAndSet(numb, numb + 1));
    }

    public int get() {
        return count.get();
    }
}
