package com.company.threadpool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
    private final Object monitor = new Object();
    private final int maxPoolSize;
    private boolean shutdown = false;
    private final LinkedList<Runnable> queue = new LinkedList<>();
    private final List<Thread> threads;

    public ThreadPool(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        this.threads = new ArrayList<>(maxPoolSize);
        start();
    }

    private void start() {
        shutdown = false;
        Thread workThread = new Thread(() -> {

            while (!shutdown || !queue.isEmpty()) {
                synchronized (monitor) {
                    if (queue.isEmpty()) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Runnable r = queue.get(0);
                for (int i = 0; i < maxPoolSize; i++) {
                    if (threads.size() <= i)
                        threads.add(new Thread(r));
                    else if (!threads.get(i).isAlive())
                        threads.set(i, new Thread(r));
                    else
                        continue;
                    threads.get(i).start();
                    queue.remove(0);
                    break;
                }
            }
        }
        );
        workThread.start();
    }

    public void shutdown() {
        shutdown = true;
    }

    public void execute(Runnable r) {
        if (shutdown)
            throw new IllegalStateException("Thread pool is shutdown");

        synchronized (monitor) {
            queue.add(r);
            monitor.notify();
        }
    }
}
