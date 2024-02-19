package com.company.threadpool;

import java.util.LinkedList;

public class ThreadPool {
    private final Object monitor = new Object();
    private final int maxPoolSize;
    private final LinkedList<Runnable> queue = new LinkedList<>();
    private boolean shutdown = false;

    public ThreadPool(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        start();
    }

    private void start() {
        for (int i = 0; i < maxPoolSize; i++) {
            Thread thread = new Thread(() -> {
                Runnable target = null;
                while (!shutdown || !queue.isEmpty()) {
                    synchronized (monitor) {
                        if (queue.isEmpty()) {
                            try {
                                monitor.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!queue.isEmpty())
                            target = queue.remove(0);
                    }
                    if (target != null)
                        target.run();
                }
                // При вызове shutdown(), если сразу после входа в цикл соседний поток достанет последнюю задачу из очереди -
                // текущий поток заснет т.к. условие входа в цикл находится вне блока synchronized.
                // Поэтому их надо разбудить перед выходом
                synchronized (monitor) {
                    monitor.notifyAll();
                }
            });
            thread.start();
        }
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
