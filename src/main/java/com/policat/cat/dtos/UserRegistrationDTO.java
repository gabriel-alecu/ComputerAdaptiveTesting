package com.policat.cat.dtos;

import com.policat.cat.validators.PasswordMatches;

import javax.validation.constraints.Size;

@PasswordMatches
public class UserRegistrationDTO implements PasswordCheckDTO {
    @Size(min=2, max=30)
    private String username;
    
    @Size(min=6)
    private String password;

    private String password_confirm;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword_confirm() {
        return password_confirm;
    }

    public void setPassword_confirm(String password_confirm) {
        this.password_confirm = password_confirm;
    }
}
