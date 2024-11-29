package com.yogosaza.oms2.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    @PostMapping("/save")
    public String saveDate(@RequestParam String key, @RequestParam String value) {
        redisService.save(key, value, 60);
        return "ok";
    }

    @GetMapping("/get")
    public String getData(@RequestParam String key) {
        return redisService.get(key);
    }

    @DeleteMapping("/delete")
    public String deleteData(@RequestParam String key) {
        redisService.delete(key);
        return "ok";
    }
}
