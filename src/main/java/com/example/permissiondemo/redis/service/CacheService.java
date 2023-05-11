package com.example.permissiondemo.redis.service;

import com.example.permissiondemo.redis.Permission;

import java.util.Optional;

public interface CacheService {
    void createPermissionCache(Permission permission);
    Optional<Permission> getPermissionCache(String key);
}
