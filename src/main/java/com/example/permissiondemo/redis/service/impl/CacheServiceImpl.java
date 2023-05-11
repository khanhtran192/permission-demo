package com.example.permissiondemo.redis.service.impl;

import com.example.permissiondemo.redis.Permission;
import com.example.permissiondemo.redis.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheServiceImpl implements CacheService {
    private final RedisTemplate<String, Permission> permissionRedisTemplate;

    private static final Duration PERMISSION_CACHE_TTL = Duration.ofHours(4);

    @Override
    public void createPermissionCache(Permission permission) {
        permissionRedisTemplate.opsForValue().set(permission.getToken(), permission, PERMISSION_CACHE_TTL);
        log.info("create cache complete");
    }

    @Override
    public Optional<Permission> getPermissionCache(String key) {
        Permission permission = permissionRedisTemplate.opsForValue().get(key);
        return Optional.ofNullable(permission);
    }
}
