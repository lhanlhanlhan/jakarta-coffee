package com.jakarta.jakartaback.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInfoRequest {
    private String old_username;
    private String username;
    private String old_password;
    private String new_password;
    private String email;
    private String telephone;

    public String getOld_username() {
        return old_username;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getNew_password() {
        return new_password;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isUpdatingPassword() {
        return this.new_password != null && this.old_password != null;
    }
}
