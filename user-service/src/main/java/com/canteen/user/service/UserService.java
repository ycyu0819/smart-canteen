package com.canteen.user.service;

import com.canteen.common.exception.BusinessException;
import com.canteen.user.dto.UserProfileResponse;
import com.canteen.user.entity.User;
import com.canteen.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepo) { this.userRepo = userRepo; }

    public UserProfileResponse getProfile(Long userId) {
        return UserProfileResponse.from(
                userRepo.findById(userId)
                        .orElseThrow(() -> new BusinessException(400, "用户不存在")));
    }

    @Transactional
    public UserProfileResponse updateProfile(Long userId, String nickname, String avatarUrl) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BusinessException(400, "用户不存在"));
        if (nickname != null && !nickname.isBlank()) user.setNickname(nickname);
        if (avatarUrl != null && !avatarUrl.isBlank()) user.setAvatarUrl(avatarUrl);
        userRepo.save(user);
        return UserProfileResponse.from(user);
    }

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BusinessException(400, "用户不存在"));
        if (!encoder.matches(oldPassword, user.getPasswordHash())) {
            throw new BusinessException(400, "旧密码错误");
        }
        user.setPasswordHash(encoder.encode(newPassword));
        userRepo.save(user);
    }

    public Page<UserProfileResponse> listUsers(int page, int size, String role, String status) {
        Page<User> users;
        if (role != null) users = userRepo.findByRole(role, PageRequest.of(page, size));
        else if (status != null) users = userRepo.findByStatus(status, PageRequest.of(page, size));
        else users = userRepo.findAll(PageRequest.of(page, size));
        return users.map(UserProfileResponse::from);
    }

    @Transactional
    public void toggleUserStatus(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BusinessException(400, "用户不存在"));
        user.setStatus("NORMAL".equals(user.getStatus()) ? "DISABLED" : "NORMAL");
        userRepo.save(user);
    }
}
