package com.epxing.auth.service.impl;

import com.epxing.auth.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenling
 * @date 2020/1/14 21:27
 * @since V1.0.0
 */
@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService {


    @Resource
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("登陆用户名：" + username);
        String password = passwordEncoder.encode("123456");
        User user1 = new User("chenling", password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        return user1;
    }

}
