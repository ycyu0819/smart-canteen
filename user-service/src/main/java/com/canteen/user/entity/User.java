package com.canteen.user.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", unique = true, length = 32)
    private String studentId;

    @Column(unique = true, nullable = false, length = 16)
    private String phone;

    @Column(name = "password_hash", nullable = false, length = 128)
    private String passwordHash;

    @Column(length = 64)
    private String nickname;

    @Column(name = "avatar_url", length = 256)
    private String avatarUrl;

    @Column(nullable = false, length = 16)
    private String role;

    @Column(nullable = false, length = 16)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (role == null) role = "CONSUMER";
        if (status == null) status = "NORMAL";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String v) { studentId = v; }
    public String getPhone() { return phone; }
    public void setPhone(String v) { phone = v; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String v) { passwordHash = v; }
    public String getNickname() { return nickname; }
    public void setNickname(String v) { nickname = v; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String v) { avatarUrl = v; }
    public String getRole() { return role; }
    public void setRole(String v) { role = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
