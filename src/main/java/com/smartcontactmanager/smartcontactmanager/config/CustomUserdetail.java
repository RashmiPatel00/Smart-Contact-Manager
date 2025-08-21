package com.smartcontactmanager.smartcontactmanager.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.smartcontactmanager.smartcontactmanager.Entities.User;


public class CustomUserdetail implements UserDetails {
    private User user;

    public CustomUserdetail(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Ensure the role is not null or empty
        if (user.getRole() == null) {
            return Collections.emptyList();
        }
        
        // Prefix the role with "ROLE_" before passing to SimpleGrantedAuthority
        String rolePrefix = "ROLE_";
        String role = rolePrefix + user.getRole().name();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);

        return List.of(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();  // Return the actual enabled status from the User entity
    }
}
