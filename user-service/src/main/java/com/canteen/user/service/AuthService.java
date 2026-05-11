package com.canteen.user.service;

import com.canteen.common.auth.JwtUtils;
import com.canteen.common.auth.Role;
import com.canteen.common.exception.BusinessException;
import com.canteen.common.response.ResultCode;
import com.canteen.user.dto.LoginRequest;
import com.canteen.user.dto.LoginResponse;
import com.canteen.user.dto.RegisterRequest;
import com.canteen.user.entity.RefreshToken;
import com.canteen.user.entity.User;
import com.canteen.user.repository.RefreshTokenRepository;
import com.canteen.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final RefreshTokenRepository refreshTokenRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepo, RefreshTokenRepository refreshTokenRepo) {
        this.userRepo = userRepo;
        this.refreshTokenRepo = refreshTokenRepo;
    }

    @Transactional
    public Long registerByPhone(RegisterRequest req) {
        if (userRepo.existsByPhone(req.getPhone())) {
            throw new BusinessException(ResultCode.PHONE_ALREADY_EXISTS);
        }
        if (!"1234".equals(req.getSmsCode())) {
            throw new BusinessException(400, "验证码错误");
        }
        User user = new User();
        user.setPhone(req.getPhone());
        user.setPasswordHash(encoder.encode(req.getPassword()));
        user.setRole(Role.CONSUMER.name());
        userRepo.save(user);
        return user.getId();
    }

    @Transactional
    public Long registerByStudentId(RegisterRequest req) {
        if (req.getStudentId() == null || req.getStudentId().isBlank()) {
            throw new BusinessException(400, "学工号不能为空");
        }
        if (userRepo.existsByStudentId(req.getStudentId())) {
            throw new BusinessException(400, "学工号已注册");
        }
        User user = new User();
        user.setStudentId(req.getStudentId());
        user.setPhone(req.getPhone());
        user.setPasswordHash(encoder.encode(req.getPassword()));
        user.setRole(Role.CONSUMER.name());
        userRepo.save(user);
        return user.getId();
    }

    public LoginResponse login(LoginRequest req) {
        User user = userRepo.findByPhone(req.getAccount())
                .or(() -> userRepo.findByStudentId(req.getAccount()))
                .orElseThrow(() -> new BusinessException(ResultCode.BAD_CREDENTIALS));

        if ("DISABLED".equals(user.getStatus())) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        if (!encoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(ResultCode.BAD_CREDENTIALS);
        }

        String accessToken = JwtUtils.generateAccessToken(user.getId(), user.getRole());
        String refreshTokenStr = JwtUtils.generateRefreshToken(user.getId());

        RefreshToken rt = new RefreshToken();
        rt.setUserId(user.getId());
        rt.setToken(refreshTokenStr);
        rt.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepo.save(rt);

        return new LoginResponse(accessToken, refreshTokenStr, 7200);
    }

    @Transactional
    public LoginResponse refreshToken(String refreshTokenStr) {
        RefreshToken rt = refreshTokenRepo.findByToken(refreshTokenStr)
                .filter(t -> !t.getRevoked() && t.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new BusinessException(ResultCode.TOKEN_EXPIRED));

        User user = userRepo.findById(rt.getUserId())
                .orElseThrow(() -> new BusinessException(ResultCode.BAD_CREDENTIALS));
        if ("DISABLED".equals(user.getStatus())) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        String newAccessToken = JwtUtils.generateAccessToken(user.getId(), user.getRole());
        String newRefreshTokenStr = JwtUtils.generateRefreshToken(user.getId());

        rt.setRevoked(true);
        refreshTokenRepo.save(rt);

        RefreshToken newRt = new RefreshToken();
        newRt.setUserId(user.getId());
        newRt.setToken(newRefreshTokenStr);
        newRt.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepo.save(newRt);

        return new LoginResponse(newAccessToken, newRefreshTokenStr, 7200);
    }

    public void logout(Long userId) {
        refreshTokenRepo.revokeAllByUserId(userId);
    }
}
