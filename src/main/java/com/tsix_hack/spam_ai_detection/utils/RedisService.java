package com.tsix_hack.spam_ai_detection.utils;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RedisService {
    private StringRedisTemplate stringRedisTemplate;

    public void setValue(String key , String value , long timeout , TimeUnit timeUnit)  {
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public String getValue(String key)  {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void deleteValue(String key)  {
        stringRedisTemplate.delete(key);
    }
}
