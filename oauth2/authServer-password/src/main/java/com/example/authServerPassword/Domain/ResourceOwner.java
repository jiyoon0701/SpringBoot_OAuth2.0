package com.example.authServerPassword.Domain;

import com.example.authServerPassword.constrant.UserRole;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Data
public class ResourceOwner implements UserDetails {

    private Long id;

    private String username;

    private String password;

    private UserRole role;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    public Collection<? extends GrantedAuthority> authorities;
}
