package com.commerce.monolithic.domain.admin.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.commerce.monolithic.domain.admin.entity.Admin;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "AdminDataResponse", description = "로그인한 관리자 본인 정보 응답 DTO")
public class AdminDataResponse {

	@Schema(description = "관리자 ID (UUID)", example = "5f1a1b44-4a4b-4c4a-92ef-8d3a5f6e2b10")
	private final UUID id;

	@Schema(description = "이메일", example = "admin@example.com")
	private final String email;

	@Schema(description = "이름", example = "홍길동")
	private final String name;

	@Schema(description = "닉네임", example = "HGD")
	private final String nickname;

	@Schema(description = "휴대폰 번호", example = "010-1234-5678")
	private final String phoneNumber;

	@Schema(description = "직책/포지션", example = "Platform Admin")
	private final String position;

	@Schema(description = "생성 일시(ISO-8601)")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private final LocalDateTime createdAt;

	@Schema(description = "수정 일시(ISO-8601)")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private final LocalDateTime updatedAt;

	public static AdminDataResponse from(Admin a) {
		return AdminDataResponse.builder()
			.id(a.getId())
			.email(a.getEmail())
			.name(a.getName())
			.nickname(a.getNickname())
			.phoneNumber(a.getPhoneNumber())
			.position(a.getPosition())
			.createdAt(a.getCreatedAt())
			.updatedAt(a.getUpdatedAt())
			.build();
	}
}
