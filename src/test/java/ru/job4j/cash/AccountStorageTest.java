package ru.job4j.cash;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AccountStorageTest {

    @Test
    void whenAdd() {
        var storage = new AccountStorage();
        assertThat(storage.add(new Account(1, 100))).isEqualTo(true);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(100);
    }

    @Test
    void whenDoubleAdd() {
        var storage = new AccountStorage();
        assertThat(storage.add(new Account(1, 100))).isEqualTo(true);
        assertThat(storage.add(new Account(1, 300))).isEqualTo(false);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(100);
    }

    @Test
    void whenUpdate() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        assertThat(storage.update(new Account(1, 300))).isEqualTo(true);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(300);
    }

    @Test
    void whenDelete() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        assertThat(storage.delete(1)).isEqualTo(true);
        assertThat(storage.getById(1)).isEmpty();
    }

    @Test
    void whenDeleteWrong() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        assertThat(storage.delete(2)).isEqualTo(false);
        assertThat(storage.getById(2)).isEmpty();
    }

    @Test
    void whenTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 200));
        storage.transfer(1, 2, 100);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 2"));
        assertThat(firstAccount.amount()).isEqualTo(0);
        assertThat(secondAccount.amount()).isEqualTo(300);
    }

    @Test
    void whenTransferToNotExistingAcc() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.transfer(1, 2, 100);
        assertThat(storage.transfer(1, 2, 100)).isEqualTo(false);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(100);
    }

    @Test
    void whenTransferButNotEnoughMoney() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        assertThat(storage.transfer(1, 2, 200)).isEqualTo(false);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 2"));
        assertThat(firstAccount.amount()).isEqualTo(100);
        assertThat(secondAccount.amount()).isEqualTo(100);
    }
}