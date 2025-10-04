package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example")
@EntityScan("com.example.entity")
@EnableJpaRepositories("com.example.repository")
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		System.out.println("\n🚀 Spring Security Demo Application Started Successfully!");
		System.out.println("🌐 Application URL: http://localhost:8080");
		System.out.println("🔧 H2 Console: http://localhost:8080/h2-console");
		System.out.println("📋 Demo Accounts:");
		System.out.println("   👤 Admin: admin/admin123");
		System.out.println("   👤 User1: user1/user123");
		System.out.println("   👤 User2: user2/user123");
		System.out.println("   👤 Moderator: moderator/mod123");
		System.out.println("===============================================\n");
	}

}
