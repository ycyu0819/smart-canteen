package com.canteen.user.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    private String account;
    @NotBlank
    private String password;

    public String getAccount() { return account; }
    public void setAccount(String v) { account = v; }
    public String getPassword() { return password; }
    public void setPassword(String v) { password = v; }
}
