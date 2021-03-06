package com.epxing.auth.config;

import com.epxing.auth.enhancer.JwtTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 授权服务器配置
 *
 * @author chenling
 * @date 2020/1/14 23:06
 * @since V1.0.0
 */
@Configuration
@EnableAuthorizationServer
public class Oauth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userService;

    @Autowired
    @Qualifier("jwtTokenStore")
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Resource
    private JwtTokenEnhancer jwtTokenEnhancer;


    private static final int ACCESS_TOKEN_TIMER = 60 * 60 * 24;


    private static final int REFRESH_TOKEN_TIMER = 60 * 60 * 24 * 30;




    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */


    /**
     * 使用密码模式
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        // 配置jwt内容增强器
        delegates.add(jwtTokenEnhancer);
        delegates.add(jwtAccessTokenConverter);
        tokenEnhancerChain.setTokenEnhancers(delegates);

        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userService)
                // 配置令牌存储策略
                .tokenStore(tokenStore)
                .accessTokenConverter(jwtAccessTokenConverter)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST)
                .tokenEnhancer(tokenEnhancerChain);

        System.out.println("OAuth2授权服务器【token存储方式和开启密码验证】配置完成！");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                // 配置client_id
                .withClient("epxing-auth")
                // 配置client_secret
                .secret(passwordEncoder.encode("epxing"))
                // 配置访问token的有效期
                .accessTokenValiditySeconds(ACCESS_TOKEN_TIMER)
                // 配置刷新token的有效期
                .refreshTokenValiditySeconds(REFRESH_TOKEN_TIMER)
                // 配置redirect_uri,用于授权成功后的跳转
//                .redirectUris("http://localhost:5555/auth-server/auth/login")
                // 自动授权配置
                .autoApprove(true)
                .resourceIds("auth-service")
                // 配置申请的权限范围
                .scopes("all")
                // 配置grant_type,表示授权类型
                .authorizedGrantTypes("authorization_code", "refresh_token", "password");

        System.out.println("OAuth2授权服务器【客户端基本信息】配置完成！");
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()");
        security.checkTokenAccess("permitAll()");
        security.checkTokenAccess("isAuthenticated()");
        security.allowFormAuthenticationForClients();

    }



}

