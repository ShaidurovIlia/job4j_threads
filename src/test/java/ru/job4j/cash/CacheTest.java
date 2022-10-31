package ru.job4j.cash;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CacheTest {

    @Test
    void whenAddTheSameAgain() {
        Base  model = new Base(1, 0);
        Cache cache = new Cache();
        assertThat(cache.add(model)).isEqualTo(true);
        assertThat(cache.add(model)).isEqualTo(false);
    }

    @Test
    void whenUpdate() {
        Base model = new Base(1, 0);
        model.setName("Artur");
        Base newModel = new Base(1, 0);
        newModel.setName("Ilya");
        Cache cache = new Cache();
        cache.add(model);
        assertThat(cache.update(newModel)).isEqualTo(true);
        assertThat(cache.getBase(1).getName()).isEqualTo("Ilya");
        assertThat(cache.getBase(1).getVersion()).isEqualTo(1);
    }

    @Test
    void whenDelete() {
        Base model = new Base(1, 0);
        Cache cache =  new Cache();
        cache.delete(model);
        assertThat(cache.getBase(1)).isEqualTo(null);
    }

    @Test
    void whenUpdateIsFailed() {
        Base model = new Base(1, 0);
        model.setName("Artur");
        Cache cache = new Cache();
        cache.add(model);
        Base upBase = new Base(1, 1);
        upBase.setName("Ilya");
        assertThatThrownBy(() -> cache
                .update(upBase))
                .isInstanceOf(OptimisticException.class);
    }
}