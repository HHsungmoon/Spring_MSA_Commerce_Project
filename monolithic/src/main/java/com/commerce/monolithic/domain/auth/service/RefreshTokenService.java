package com.commerce.monolithic.domain.auth.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.commerce.monolithic.domain.admin.entity.Admin;
import com.commerce.monolithic.domain.admin.repository.AdminRepository;
import com.commerce.monolithic.domain.catalogstore.entity.Manager;
import com.commerce.monolithic.domain.catalogstore.repository.ManagerRepository;
import com.commerce.monolithic.domain.customer.entity.Customer;
import com.commerce.monolithic.domain.customer.repository.CustomerRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RefreshTokenService {

	//private final RedisTemplate<String, Object> redisTemplate;
	private final AdminRepository adminRepository;
	private final ManagerRepository managerRepository;
	private final CustomerRepository customerRepository;

	private static final String BLACKLIST_PREFIX = "blacklist:";

	private String createKey(Object user) {
		String prefix;
		String id;

		if (user instanceof Customer) {
			prefix = "customer:";
			id = ((Customer)user).getId().toString();
		} else if (user instanceof Manager) {
			prefix = "manager:";
			id = ((Manager)user).getId().toString();
		} else if (user instanceof Admin) {
			prefix = "admin:";
			id = ((Admin)user).getId().toString();
		} else {
			throw new IllegalArgumentException("Unknown user type");
		}

		return "refresh:" + prefix + id;
	}

	public Object findUserByRoleAndId(String role, UUID id) {
		return switch (role) {
			case "admin" -> adminRepository.findById(id).orElseThrow(() -> new RuntimeException("관리자 정보 없음"));
			case "manager" -> managerRepository.findById(id).orElseThrow(() -> new RuntimeException("매니저 정보 없음"));
			case "customer" -> customerRepository.findById(id).orElseThrow(() -> new RuntimeException("고객 정보 없음"));
			default -> throw new IllegalArgumentException("알 수 없는 역할: " + role);
		};
	}

	public void saveOrUpdateToken(Object user, String refreshToken, LocalDateTime expiryDateTime) {
		String key = createKey(user);
		long duration = Duration.between(LocalDateTime.now(), expiryDateTime).getSeconds();
		//redisTemplate.opsForValue().set(key, refreshToken, duration, TimeUnit.SECONDS);
	}

	/*
	public boolean isValid(Object user, String token) {
		String key = createKey(user);
		Object stored = redisTemplate.opsForValue().get(key);

		if (redisTemplate.hasKey(BLACKLIST_PREFIX + token)) {
			return false;
		}

		return stored != null && stored.equals(token);
	}


	public void addToBlacklist(String token, long expirationSeconds) {
		redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "blacklisted", expirationSeconds, TimeUnit.SECONDS);
	}

	public void delete(Object user) {
		String key = createKey(user);
		redisTemplate.delete(key);
	}
	*/
}
