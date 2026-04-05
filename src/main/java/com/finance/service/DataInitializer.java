package com.finance.service;

import com.finance.entity.Role;
import com.finance.entity.User;
import com.finance.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        userRepository.save(new User(null, "admin", "admin@finance.local", Role.ADMIN, true, null, null));
        userRepository.save(new User(null, "analyst", "analyst@finance.local", Role.ANALYST, true, null, null));
        userRepository.save(new User(null, "viewer", "viewer@finance.local", Role.VIEWER, true, null, null));
    }
}
