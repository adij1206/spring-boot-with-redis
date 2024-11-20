package com.aditya.springbootredis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisValueCache {

    private final ValueOperations<String, Object> valueOperations;

    public RedisValueCache(RedisTemplate<String, Object> redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void cache(String key, Object value) {
        this.valueOperations.set(key, value);
    }

    public Object getValue(String key) {
        return this.valueOperations.get(key);
    }

    public void deleteCachedKey(String key) {
        this.valueOperations.getOperations().delete(key);
    }
}
