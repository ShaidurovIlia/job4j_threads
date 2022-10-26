package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {

    @GuardedBy("this")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        return Objects.isNull(accounts.putIfAbsent(account.id(), account));
    }

    public synchronized boolean update(Account account) {
        return !Objects.isNull(accounts.replace(account.id(), account));
    }

    public synchronized boolean delete(int id) {
        return !Objects.isNull(accounts.remove(id));
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        boolean result = false;
        Optional<Account> from = getById(fromId);
        Optional<Account> to = getById(toId);

        if ((from.isPresent())
                && (to.isPresent())
                && (from.get().amount() >= amount)) {
            update(new Account(fromId, from.get().amount() - amount));
            update(new Account(toId, to.get().amount() + amount));
            result = true;
        }
        return result;
    }
}
