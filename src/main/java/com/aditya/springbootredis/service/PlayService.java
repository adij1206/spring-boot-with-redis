package com.aditya.springbootredis.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class PlayService {

    private final DistributedLocker distributedLocker;

    public PlayService(DistributedLocker distributedLocker) {
        this.distributedLocker = distributedLocker;
    }

//    @PostConstruct
//    public void execute() {
//        CompletableFuture.runAsync(() -> runTask("Task1", 3000));
//        CompletableFuture.runAsync(() -> runTask("Task2", 1000));
//        CompletableFuture.runAsync(() -> runTask("Task3", 100));
//
//    }


//    private void runTask(String taskName, long sleep) {
//        System.out.println("Running task " + taskName);
//
//        distributedLocker.lock("some-key", 6, 5, () -> {
//            System.out.println("Sleeping for" + sleep + "ms");
//            Thread.sleep(sleep);
//            System.out.println("Executing task" + taskName);
//            return taskName;
//        });
//    }

    @PostConstruct
    public void execute() {
        CompletableFuture.runAsync(() -> runTask("Task1","key1", 3000));
        CompletableFuture.runAsync(() -> runTask("Task2", "key1", 1000));
        CompletableFuture.runAsync(() -> runTask("Task3", "key2", 500));
    }

    /**
     * In below method we are sleeping the thread for more than the ttl of the cache,
     * so the other thread will be able to acquire the lock before the execution of Task1 will be completed.
     */
    //    @PostConstruct
//    public void execute() {
//        CompletableFuture.runAsync(() -> runTask("Task1","key1", 4000));
//        CompletableFuture.runAsync(() -> runTask("Task2", "key1", 5000));
//        CompletableFuture.runAsync(() -> runTask("Task3", "key2", 500));
//
//    }

    private void runTask(String taskName, String key,  long sleep) {
        System.out.println("Running task " + taskName);

        distributedLocker.lock(key, 3, 5, () -> {
            System.out.println("Sleeping for" + sleep + "ms");
            Thread.sleep(sleep);
            System.out.println("Executing task" + taskName);
            return taskName;
        });
    }
}
