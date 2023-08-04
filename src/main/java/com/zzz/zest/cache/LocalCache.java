package com.zzz.zest.cache;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocalCache {

    private final Map<String, String> accessTokenCache = new HashMap<>();

    public String get (String key) {
        return accessTokenCache.get(key);
    }

    public String set (String key, String value) {
        return accessTokenCache.put(key, value);
    }


}
