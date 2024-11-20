package com.aditya.springbootredis.api;

import com.aditya.springbootredis.dto.Person;
import com.aditya.springbootredis.service.RedisValueCache;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/redis-value", produces = MediaType.APPLICATION_JSON_VALUE)
public class RedisValueController {

    private final RedisValueCache redisValueCache;

    public RedisValueController(RedisValueCache redisValueCache) {
        this.redisValueCache = redisValueCache;
    }

    @PostMapping
    public void cachePerson(@RequestBody Person person) {
        System.out.println(person);
        redisValueCache.cache(person.getId(), person);
    }

    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable("id") String id) {
        return (Person) redisValueCache.getValue(id);
    }
}
