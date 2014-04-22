package com.cometproject.server.cache.handlers;

import com.cometproject.server.cache.CacheHandler;
import com.cometproject.server.cache.CacheProvider;
import com.cometproject.server.network.messages.types.Composer;

import java.util.concurrent.TimeUnit;

/**
 * Created by Matty on 22/04/2014.
 */
public class ComposerCacheHandler implements CacheHandler<String, Composer> {
    private final CacheProvider provider;

    public ComposerCacheHandler(CacheProvider provider) {
        this.provider = provider;
    }

    @Override
    public void put(String key, Composer value) {
        this.provider.put(key, value);
    }

    @Override
    public void put(String key, Composer value, int expires) {
        this.provider.put(key, value, expires);
    }

    @Override
    public void put(String key, Composer value, int expires, TimeUnit unit) {
        this.provider.put(key, value, expires, unit);
    }

    @Override
    public Composer get(String key) {
        Object o = this.provider.get(key);
        return (Composer) o;
    }

    @Override
    public boolean exists(String key) {
        return this.provider.exists(key);
    }

    @Override
    public CacheProvider getProvider() {
        return this.provider;
    }
}
