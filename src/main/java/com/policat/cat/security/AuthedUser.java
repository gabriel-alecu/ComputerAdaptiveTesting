package com.policat.cat.security;

import com.policat.cat.entities.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class AuthedUser extends org.springframework.security.core.userdetails.User {
    private User user;

    public AuthedUser(User user) {
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Role getRole() {
        return user.getRole();
    }
}