package com.sdl.dxa.caching;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableCaching
@Profile("!dxa.no-cache")
@Slf4j
@ComponentScan(basePackages = "com.sdl.dxa.caching")
public class TridionCacheConfiguration extends CachingConfigurerSupport {

    private final LocalizationAwareKeyGenerator localizationAwareKeyGenerator;

    private final NamedCacheProvider defaultCacheProvider;

    @Autowired
    @Qualifier("compositeCacheManager")
    CacheManager compositeCacheManager;

    @Autowired
    public TridionCacheConfiguration(LocalizationAwareKeyGenerator localizationAwareKeyGenerator,
                                     NamedCacheProvider defaultCacheProvider) {
        this.localizationAwareKeyGenerator = localizationAwareKeyGenerator;
        this.defaultCacheProvider = defaultCacheProvider;
    }

    @Bean(name="compositeCacheManager")
    @Override
    public CacheManager cacheManager() {
        CompositeCacheManager compositeCacheManager = new CompositeCacheManager(
                new SpringJCacheManagerAdapter(defaultCacheProvider));
        compositeCacheManager.setFallbackToNoOpCache(true);
        return compositeCacheManager;
    }

    @Override
    public KeyGenerator keyGenerator() {
        return localizationAwareKeyGenerator;
    }

    @Bean
    @Override
    public CacheResolver cacheResolver() {
        return context -> context.getOperation().getCacheNames().stream()
                .peek(name -> log.debug("Requested cache name = '{}', cache manager caches = {}", name, compositeCacheManager.getCacheNames()))
                .map(name -> {
                    Cache cache = compositeCacheManager.getCache(name);
                    if (cache == null) {
                        log.warn("Cache {} is not found", name);
                        return null;
                    }
                    log.debug("Resolved cache {} which is a {} cache", cache.getName(), cache.getClass());
                    return cache;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
