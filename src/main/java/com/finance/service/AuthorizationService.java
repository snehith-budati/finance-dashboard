package com.finance.service;

import com.finance.entity.Role;
import com.finance.entity.User;
import com.finance.exception.ForbiddenException;
import com.finance.exception.UnauthorizedException;
import com.finance.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User requireViewer(Long userId) {
        return requireOneOf(userId, Role.VIEWER, Role.ANALYST, Role.ADMIN);
    }

    public User requireAnalyst(Long userId) {
        return requireOneOf(userId, Role.ANALYST, Role.ADMIN);
    }

    public User requireAdmin(Long userId) {
        return requireOneOf(userId, Role.ADMIN);
    }

    private User requireOneOf(Long userId, Role... allowedRoles) {
        if (userId == null) {
            throw new UnauthorizedException("Missing authentication header: X-User-Id");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Invalid user id"));

        if (!user.isActive()) {
            throw new ForbiddenException("User is inactive");
        }

        for (Role allowedRole : allowedRoles) {
            if (user.getRole() == allowedRole) {
                return user;
            }
        }

        throw new ForbiddenException("Insufficient permissions for this action");
    }
}
