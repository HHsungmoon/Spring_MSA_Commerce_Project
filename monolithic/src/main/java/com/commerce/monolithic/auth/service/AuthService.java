package com.commerce.monolithic.auth.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.commerce.monolithic.auth.dto.LoginRequestDto;
import com.commerce.monolithic.auth.dto.LoginResponseDto;
import com.commerce.monolithic.auth.dto.SignupRedisData;
import com.commerce.monolithic.auth.dto.SignupRequestDto;
import com.commerce.monolithic.auth.response.AuthErrorCode;
import com.commerce.monolithic.autoresponse.error.BusinessException;
import com.commerce.monolithic.domain.admin.entity.Admin;
import com.commerce.monolithic.domain.admin.repository.AdminRepository;
import com.commerce.monolithic.domain.catalogstore.entity.Manager;
import com.commerce.monolithic.domain.catalogstore.repository.ManagerRepository;
import com.commerce.monolithic.domain.customer.entity.Customer;
import com.commerce.monolithic.domain.customer.repository.CustomerRepository;
import com.commerce.monolithic.security.JwtTokenProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

	private final CustomerRepository customerRepository;
	private final AdminRepository adminRepository;
	private final ManagerRepository managerRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	//private final RedisTemplate<String, Object> redisTemplate;
	private final MailService mailService;
	private final RefreshTokenService refreshTokenService;

	// 1) 로그인
	public LoginResponseDto login(LoginRequestDto data) {
		// 1) Admin 먼저 조회
		Admin admin = adminRepository.findByEmail(data.getEmail())
			.orElse(null);
		if (admin != null) {
			if (!passwordEncoder.matches(data.getPassword(), admin.getPassword())) {
				throw new BusinessException(AuthErrorCode.INVALID_CREDENTIALS);
			}
			// Admin 전용 토큰(리프레시 토큰은 사용하지 않음)
			String accessToken = jwtTokenProvider.createToken(admin.getId(), "admin");
			return new LoginResponseDto(accessToken, null, "admin");
		}

		// 2) Manager 조회
		Manager manager = managerRepository.findByEmail(data.getEmail())
			.orElse(null);
		if (manager != null) {
			if (!passwordEncoder.matches(data.getPassword(), manager.getPassword())) {
				throw new BusinessException(AuthErrorCode.INVALID_CREDENTIALS);
			}
			// 방문 로그 기록 등 필요시 추가
			// User/Manager용 Access + Refresh 토큰 생성
			String accessToken = jwtTokenProvider.createToken(manager.getId(), "manager");
			String refreshToken = jwtTokenProvider.createRefreshToken(manager.getId(), "manager");
			// 리프레시 토큰 DB에 저장 또는 갱신 (추후 활성화)
			LocalDateTime expiryDate = LocalDateTime.now().plusDays(7);
			refreshTokenService.saveOrUpdateToken(manager, refreshToken, expiryDate);
			return new LoginResponseDto(accessToken, refreshToken, "manager");
		}

		// 3) Customer 조회
		Customer customer = customerRepository.findByEmail(data.getEmail())
			.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다: " + data.getEmail()));

		if (!passwordEncoder.matches(data.getPassword(), customer.getPassword())) {
			throw new BusinessException(AuthErrorCode.INVALID_CREDENTIALS);
		}

		// 방문 로그 기록
		//visitLogService.logVisit(user.getId());

		// Access + Refresh 토큰 생성
		String accessToken = jwtTokenProvider.createToken(customer.getId(), "customer");
		String refreshToken = jwtTokenProvider.createRefreshToken(customer.getId(), "customer");

		// 리프레시 토큰 DB에 저장 또는 갱신
		LocalDateTime expiryDate = LocalDateTime.now().plusDays(7);
		refreshTokenService.saveOrUpdateToken(customer, refreshToken, expiryDate);

		return new LoginResponseDto(accessToken, refreshToken, "customer");
	}

	// 2) 회원가입 (Customer 예시)
	public void tempSignup(SignupRequestDto req) {
		if (customerRepository.findByEmail(req.getEmail()).isPresent()) {
			throw new BusinessException(AuthErrorCode.INVALID_CREDENTIALS);
		}

		String verificationCode = UUID.randomUUID().toString().substring(0, 6); // 간단한 코드

		// 이메일 본문 작성
		String subject = "이메일 인증 코드";
		String text = "회원가입 인증 코드: " + verificationCode;

		// 이메일 전송
		try {
			mailService.sendMail(req.getEmail(), subject, text);
		} catch (MailException e) {
			throw new BusinessException(AuthErrorCode.INVALID_CREDENTIALS);
		}

		// Redis에 저장 (key: "signup:{email}", value: SignupRequestDto+코드)
		SignupRedisData data = new SignupRedisData(req, verificationCode);
		//redisTemplate.opsForValue().set("signup:" + req.getEmail(), data, 10, TimeUnit.MINUTES); // 10분 유효
	}

	public void signup(SignupRequestDto req) {
		if (customerRepository.findByEmail(req.getEmail()).isPresent()) {
			throw new BusinessException(AuthErrorCode.INVALID_CREDENTIALS);
		}

		Customer user = Customer.builder()
			.email(req.getEmail())
			.password(passwordEncoder.encode(req.getPassword()))
			.name(req.getName())
			.build();

		customerRepository.save(user);
	}

}
