package com.epxing.auth.config;//package com.epxing.zuul.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
//
///**
// * 使用redis 存储 token，暂时不用，先注释掉
// * @author chenling
// * @date 2020/1/15 0:35
// * @since V1.0.0
// */
//@Configuration
//public class RedisTokenStoreConfig {
//
//    @Autowired
//    private RedisConnectionFactory redisConnectionFactory;
//
//    @Bean
//    public TokenStore redisTokenStore() {
//        return new RedisTokenStore(redisConnectionFactory);
//    }
//
//}
