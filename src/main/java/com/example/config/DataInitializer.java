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

import java.util.HashSet;
import java.util.Set;

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
            Set.of(RoleName.ROLE_ADMIN, RoleName.ROLE_USER)
        );

        // Tạo user thường 1
        createUserIfNotExists(
            "user1", 
            "user123", 
            "Nguyễn Văn A", 
            "user1@example.com", 
            "0987654321",
            Set.of(RoleName.ROLE_USER)
        );

        // Tạo user thường 2
        createUserIfNotExists(
            "user2", 
            "user123", 
            "Trần Thị B", 
            "user2@example.com", 
            "0111222333",
            Set.of(RoleName.ROLE_USER)
        );

        // Tạo moderator user
        createUserIfNotExists(
            "moderator", 
            "mod123", 
            "Lê Văn C", 
            "moderator@example.com", 
            "0444555666",
            Set.of(RoleName.ROLE_MODERATOR, RoleName.ROLE_USER)
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
                                     String email, String phoneNumber, Set<RoleName> roleNames) {
        if (!userRepository.findByUsername(username).isPresent()) {
            // Tạo user trước
            User user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .enabled(true)
                    .roles(new HashSet<>()) // Khởi tạo rỗng trước
                    .build();

            // Lưu user trước
            User savedUser = userRepository.save(user);

            // Sau đó thêm roles
            Set<Role> roles = new HashSet<>();
            for (RoleName roleName : roleNames) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                roles.add(role);
            }
            
            // Cập nhật roles cho user đã lưu
            savedUser.setRoles(roles);
            userRepository.save(savedUser);

            System.out.println("👤 Tạo user: " + username + " (" + name + ") với roles: " + roleNames);
        }
    }
}