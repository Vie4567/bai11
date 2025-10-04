package com.example.config;

import com.example.entity.Role;
import com.example.entity.Role.RoleName;
import com.example.entity.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Tạo roles nếu chưa tồn tại
        createRoleIfNotExists(RoleName.ROLE_USER);
        createRoleIfNotExists(RoleName.ROLE_ADMIN);
        createRoleIfNotExists(RoleName.ROLE_MODERATOR);

        // Tạo admin user
        createUserIfNotExists(
            "admin", 
            "admin123", 
            "Administrator", 
            "admin@example.com", 
            "0123456789",
            RoleName.ROLE_ADMIN
        );

        // Tạo user thường 1
        createUserIfNotExists(
            "user1", 
            "user123", 
            "Nguyễn Văn A", 
            "user1@example.com", 
            "0987654321",
            RoleName.ROLE_USER
        );

        // Tạo user thường 2
        createUserIfNotExists(
            "user2", 
            "user123", 
            "Trần Thị B", 
            "user2@example.com", 
            "0111222333",
            RoleName.ROLE_USER
        );

        // Tạo moderator user
        createUserIfNotExists(
            "moderator", 
            "mod123", 
            "Lê Văn C", 
            "moderator@example.com", 
            "0444555666",
            RoleName.ROLE_MODERATOR
        );

        System.out.println("✅ Khởi tạo dữ liệu mẫu thành công!");
        System.out.println("📋 Danh sách tài khoản demo:");
        System.out.println("👤 Admin: username=admin, password=admin123");
        System.out.println("👤 User 1: username=user1, password=user123");
        System.out.println("👤 User 2: username=user2, password=user123");
        System.out.println("👤 Moderator: username=moderator, password=mod123");
    }

    private void createRoleIfNotExists(RoleName roleName) {
        if (!roleRepository.findByName(roleName).isPresent()) {
            Role role = Role.builder()
                    .name(roleName)
                    .build();
            roleRepository.save(role);
            System.out.println("🔧 Tạo role: " + roleName);
        }
    }

    private void createUserIfNotExists(String username, String password, String name, 
                                     String email, String phoneNumber, RoleName roleName) {
        if (!userRepository.findByUsername(username).isPresent()) {
            // Lấy role
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

            // Tạo user
            User user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .enabled(true)
                    .role(role)
                    .build();

            userRepository.save(user);

            System.out.println("👤 Tạo user: " + username + " (" + name + ") với role: " + roleName);
        }
    }
}