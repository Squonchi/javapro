package com.company;

import com.company.threadpool.ThreadPool;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(10);
        Random random = new Random();


        // Если размер пула больше или равен кол-ву итераций цикла
        // То: длительность сна каждого потока в консоли будет идти по нарастающей
        // Иначе: рандомно
        for (int i = 0; i < 10; i++)
            threadPool.execute(() -> {
                int sleepTime = random.nextInt(10);
                try {
                    Thread.sleep(sleepTime * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " спал " + sleepTime + " секунд");
            });
        threadPool.shutdown();
//        threadPool.execute(() -> System.out.println(Thread.currentThread().getName()));
    }
}
