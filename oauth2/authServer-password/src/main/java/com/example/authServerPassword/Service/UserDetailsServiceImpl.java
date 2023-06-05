package com.example.authServerPassword.Service;


import com.example.authServerPassword.Domain.ResourceOwner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired private MemberService service;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("UserDetailsServiceImpl.loadUserByUsername :::: {}",username);

        ResourceOwner user = null;
        try {
            user = service.findByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(ObjectUtils.isEmpty(user)){
            throw new UsernameNotFoundException("Invalid resource owner, please check resource owner info !");
        }

        user.setAuthorities(AuthorityUtils.createAuthorityList(String.valueOf(user.getRole())));

        return user;
    }
}
