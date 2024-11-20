package com.aditya.springbootredis.api;

import com.aditya.springbootredis.dto.Person;
import com.aditya.springbootredis.dto.Range;
import com.aditya.springbootredis.service.RedisListCache;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/redis-list", produces = MediaType.APPLICATION_JSON_VALUE)
public class RedisListController {

    private final RedisListCache redisListCache;

    public RedisListController(RedisListCache redisListCache) {
        this.redisListCache = redisListCache;
    }

    @PostMapping("/{key}")
    public void cachePersons(@PathVariable("key") String key, @RequestBody List<Person> persons) {
        redisListCache.cachePersons(key, persons);
    }

    @PostMapping("/range/{key}")
    public List<Person> getPersons(@PathVariable("key") String key, @RequestBody Range range) {
        return redisListCache.getPersons(key, range);
    }

    @GetMapping("/{key}")
    public Person getLastPerson(@PathVariable("key") String key) {
        return redisListCache.getLastPerson(key);
    }

    @PostMapping("/trim/{key}")
    public void trim(@PathVariable("key") String key, @RequestBody Range range) {
        redisListCache.trim(key, range);
    }
}
