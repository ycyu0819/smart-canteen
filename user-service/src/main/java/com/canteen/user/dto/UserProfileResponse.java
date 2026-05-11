package com.canteen.user.dto;

import com.canteen.user.entity.User;

public class UserProfileResponse {
    private Long id;
    private String phone;
    private String studentId;
    private String nickname;
    private String avatarUrl;
    private String role;

    public static UserProfileResponse from(User user) {
        UserProfileResponse r = new UserProfileResponse();
        r.id = user.getId();
        r.phone = user.getPhone();
        r.studentId = user.getStudentId();
        r.nickname = user.getNickname();
        r.avatarUrl = user.getAvatarUrl();
        r.role = user.getRole();
        return r;
    }

    public Long getId() { return id; }
    public String getPhone() { return phone; }
    public String getStudentId() { return studentId; }
    public String getNickname() { return nickname; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getRole() { return role; }
}
