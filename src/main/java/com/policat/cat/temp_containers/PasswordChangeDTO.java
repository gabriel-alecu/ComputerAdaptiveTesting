package com.policat.cat.temp_containers;

import com.policat.cat.validators.PasswordMatches;

import javax.validation.constraints.Size;

@PasswordMatches
public class PasswordChangeDTO implements PasswordCheckDTO {
    private String old_password;

    @Size(min=6)
    private String password;

    private String password_confirm;

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
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
