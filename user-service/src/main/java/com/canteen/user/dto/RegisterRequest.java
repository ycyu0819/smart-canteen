package com.canteen.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank @Size(min = 11, max = 11)
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Size(min = 3, max = 32)
    private String studentId;

    @NotBlank @Size(min = 6, max = 32)
    private String password;

    @NotBlank @Size(min = 4, max = 6)
    private String smsCode;

    public String getPhone() { return phone; }
    public void setPhone(String v) { phone = v; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String v) { studentId = v; }
    public String getPassword() { return password; }
    public void setPassword(String v) { password = v; }
    public String getSmsCode() { return smsCode; }
    public void setSmsCode(String v) { smsCode = v; }
}
