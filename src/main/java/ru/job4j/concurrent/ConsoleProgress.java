package ru.job4j.concurrent;

import java.lang.Runnable;

public class ConsoleProgress implements Runnable {

    @Override
    public void run() {
        String[] process = {"\\|", "|/"};
        int index = 0;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.print("\r loading: " + process[index++]);
                Thread.sleep(100);
                if (index == process.length) {
                    index = 0;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(10000);
        progress.interrupt();
    }
}
