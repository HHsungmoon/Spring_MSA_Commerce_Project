package com.commerce.monolithic.domain.admin.dto.storeapp;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.commerce.monolithic.configenum.GlobalEnum.StoreStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "StoreApplicationSearchRequest", description = "매니저 신청 목록 조회 조건")
public class StoreApplicationSearchRequest {

	@Schema(description = "상태 필터", example = "PENDING", allowableValues = {"PENDING", "APPROVED", "REJECTED"})
	private StoreStatus status;

	@Schema(description = "시작일(포함)", example = "2025-09-01")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate from;

	@Schema(description = "종료일(포함)", example = "2025-09-30")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate to;

	@Schema(description = "페이지(0-base)", example = "0", defaultValue = "0")
	@Min(0)
	private Integer page = 0;

	@Schema(description = "페이지 크기", example = "20", defaultValue = "20")
	@Min(1)
	@Max(200)
	private Integer size = 20;
}
