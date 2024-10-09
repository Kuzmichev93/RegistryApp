package app.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisConfiguration {



    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("redis",6379);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisCacheManager cacheManager(){
        RedisCacheConfiguration cacheConfiguration = myDefaultCacheConfig(Duration.ofMinutes(10)).disableCachingNullValues();
        return RedisCacheManager.builder(lettuceConnectionFactory())
                .cacheDefaults(cacheConfiguration)
                .withCacheConfiguration("set5min",myDefaultCacheConfig(Duration.ofMinutes(5)))
                .withCacheConfiguration("set1min",myDefaultCacheConfig(Duration.ofMinutes(1)))
                .build();
    }


    private RedisCacheConfiguration myDefaultCacheConfig(Duration duration){
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(duration)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }


}
