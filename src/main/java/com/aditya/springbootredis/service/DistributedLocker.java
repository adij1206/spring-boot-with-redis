package com.aditya.springbootredis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class DistributedLocker {

    private final ValueOperations<String, String> valueOperations;

    public DistributedLocker(RedisTemplate<String, String> redisTemplate) {
        valueOperations = redisTemplate.opsForValue();
    }

    public <T> LockExecutionResult<T> lock(
            String key, int lockTimeoutSeconds, int lockRequiredRetryTimeSeconds, Callable<T> task
    ) {
        try {
            return tryLockAcquired(() -> {
                Boolean lockAcquired = valueOperations.setIfAbsent(key, key, lockTimeoutSeconds, TimeUnit.SECONDS);

                if (!lockAcquired) {
                    System.out.println("Failed to acquired lock for the key : " + key);
                    return null;
                }

                System.out.println("Locked acquired successfully for the key : " + key);

                try {
                    T taskResult = task.call();
                    return LockExecutionResult.buildLockAcquiredResult(taskResult);
                } catch (Exception e) {
                    System.out.println("Exception in executing the task" + e.getMessage());
                    return LockExecutionResult.buildLockAcquiredWithException(e);
                } finally {
                    releaseLock(key);
                }

            }, key, lockRequiredRetryTimeSeconds);
        } catch (Exception e) {
            System.out.println("Exception in executing the lock" + e.getMessage());
            return LockExecutionResult.buildLockAcquiredWithException(e);
        }
    }

    private void releaseLock(String key) {
        valueOperations.getOperations().delete(key);
    }

    private <T> T tryLockAcquired(Supplier<T> task, String key, int lockRequiredRetryTimeSeconds) throws Exception {
        final long tryToGetLockTimeout = TimeUnit.SECONDS.toMillis(lockRequiredRetryTimeSeconds);
        Long startTime = System.currentTimeMillis();

        while (true) {
            System.out.println("Trying to acquire lock for the key : " + key);
            T response = task.get();

            if (response != null) {
                return response;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (System.currentTimeMillis() - startTime > tryToGetLockTimeout) {
                throw new Exception("Failed to acquire lock in " + tryToGetLockTimeout + " milliseconds");
            }
        }
    }
}
