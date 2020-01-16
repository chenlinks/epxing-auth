package com.epxing.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsUtils;

import javax.annotation.Resource;

/**
 * 资源服务器配置
 *
 * @author chenling
 * @date 2020/1/15 0:41
 * @since V1.0.0
 */
@Configuration
@EnableResourceServer
public class Oauth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler defaultAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler defaultAuthenticationFailureHandler;

    @Autowired
    private AccessDeniedHandler defaultAuthenticationAccessDeniedHandler;

    @Autowired
    private OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // http basic认证
        http.httpBasic().disable();
        http.csrf().disable();

        // 用户名密码表单登录配置
        http.formLogin()
                // 自定义表单用户名密码认证请求处理的url
                .loginProcessingUrl("/login")
                // 自定义当请求需要身份认证时默认跳转的url
                .loginPage("/authentication/require")
                // 认证通过后的处理器
                .successHandler(defaultAuthenticationSuccessHandler)
                // 认证失败后的处理器
                .failureHandler(defaultAuthenticationFailureHandler);


        // 鉴权配置1: 配置请求白名单
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .antMatchers(HttpMethod.GET)
                .permitAll()
                .antMatchers(HttpMethod.POST)
                .permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest)
                .permitAll();

        http.authorizeRequests()
                .antMatchers("/login", "/authentication/require", "/public/**"
                        ,"/webjars/**", "/v2/**", "/swagger-resources/**", "/favicon.icon", "/swagger-ui.html")
                .permitAll();

        // 鉴权配置2.1: 配置具体业务相关默认的一些鉴权信息
        http.authorizeRequests().antMatchers("/user/**").hasRole("ADMIN")
                .antMatchers("/order/**").hasRole("USER")
                .antMatchers("/product/**").hasRole("OPERATE");

        // 鉴权配置2.2: 配置具体业务相关基于RBAC模型的鉴权信息
        // http.authorizeRequests().anyRequest().access("@permissionService.hasPermission(request, authentication)");

        // 鉴权配置3: 配置其它的任何请求需要认证信息
        http.authorizeRequests().anyRequest().authenticated();

        // 鉴权不通过时的处理器
        http.exceptionHandling().accessDeniedHandler(defaultAuthenticationAccessDeniedHandler);
    }



    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.expressionHandler(oAuth2WebSecurityExpressionHandler);
        resources.resourceId("auth-service");
    }
}
