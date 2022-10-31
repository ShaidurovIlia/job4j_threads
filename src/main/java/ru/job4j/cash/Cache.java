package ru.job4j.cash;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.getId(), model) == null;
    }

    public boolean update(Base model) {

        BiFunction<Integer, Base, Base> func = (key, value) -> {
            if (value.getVersion() != model.getVersion()) {
                throw new OptimisticException("Version are not equal");
            }
            Base temp = new Base(key, value.getVersion() + 1);
            temp.setName(model.getName());
            return temp;
        };
        return memory.computeIfPresent(model.getId(), func) != null;
    }

    public boolean delete(Base model) {
        return memory.remove(model.getId(), model);
    }

    public Base getBase(Integer id) {
        return memory.get(id);
    }
}
