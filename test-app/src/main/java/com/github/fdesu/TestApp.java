package com.github.fdesu;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestApp {
    private static final Lock LOCK = new ReentrantLock();

    public static void main(String[] args) {
        print();
    }

    private static synchronized void print() {
        synchronized (TestApp.class) {
            try {
                LOCK.lock();
                System.out.println("Test app has been launched!");
            } finally {
                LOCK.unlock();
            }
        }
    }


}
