package com.example.authenticationserver.entity;

import com.example.authenticationserver.constrant.UserRole;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;


@Data
@Entity
@Table(name = "RESOURCE_OWNER")
public class ResourceOwner implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String useremail;

    @Column(length=400)
    private String password;

    @Column@Enumerated(EnumType.STRING)
    private UserRole role;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;
}
