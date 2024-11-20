package com.aditya.springbootredis.service;

import com.aditya.springbootredis.dto.Person;
import com.aditya.springbootredis.dto.Range;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedisListCache {

    private ListOperations<String, Object> listOperations;

    public RedisListCache(RedisTemplate<String, Object> redisTemplate) {
        this.listOperations = redisTemplate.opsForList();
    }

    public void cachePersons(String key, List<Person> persons) {
        for (Person person : persons) {
            listOperations.leftPush(key, person);
        }
    }

    public List<Person> getPersons(String key, Range range) {
        List<Object> objects = listOperations.range(key, range.getFrom(), range.getTo());

        if (objects == null) {
            return new ArrayList<>();
        }

        return objects.stream()
                .map(o -> (Person) o)
                .collect(Collectors.toList());
    }

    public Person getLastPerson(String key) {
        Object object = listOperations.rightPop(key);

        if (object == null) {
            return null;
        }

        return (Person) object;
    }

    public void trim(String key, Range range) {
        listOperations.trim(key, range.getFrom(), range.getTo());
    }
}
