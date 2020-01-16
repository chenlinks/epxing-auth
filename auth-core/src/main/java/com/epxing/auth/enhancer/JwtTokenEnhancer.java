package com.epxing.auth.enhancer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenling
 * @date 2020/1/15 0:17
 * @since V1.0.0
 */
@Component
@Slf4j
public class JwtTokenEnhancer implements TokenEnhancer {


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication authentication) {
        String userName = authentication.getUserAuthentication().getName();
        System.out.println("------增强器发来消息-----------"+userName+"-------------------");
        // 得到用户名，去处理数据库可以拿到当前用户的信息和角色信息（需要传递到服务中用到的信息）
        final Map<String, Object> additionalInformation = new HashMap<>();
        // Map假装用户实体.实际应用中，需要把用户信息写入
        Map<String, String> userinfo = new HashMap<>();
        userinfo.put("id", "1");
        userinfo.put("username", "chenling");
        userinfo.put("qq", "438944209");
        userinfo.put("userFlag", "1");
        additionalInformation.put("userinfo", userinfo);

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInformation);
        return oAuth2AccessToken;
    }
}
