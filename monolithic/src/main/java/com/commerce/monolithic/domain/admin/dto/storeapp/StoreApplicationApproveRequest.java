package com.commerce.monolithic.domain.admin.dto.storeapp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "StoreApplicationApproveRequest", description = "매니저 승인 요청(선택)")
public class StoreApplicationApproveRequest {

	@Schema(description = "관리자 메모(선택)", example = "서류 확인 완료. 승인합니다.")
	private String note;
}
