package com.commerce.monolithic.domain.admin.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.monolithic.configenum.GlobalEnum.StoreStatus;
import com.commerce.monolithic.domain.admin.dto.storeapp.StoreApplicationApproveRequest;
import com.commerce.monolithic.domain.admin.dto.storeapp.StoreApplicationDetailResponse;
import com.commerce.monolithic.domain.admin.dto.storeapp.StoreApplicationPageResponse;
import com.commerce.monolithic.domain.admin.dto.storeapp.StoreApplicationRejectRequest;
import com.commerce.monolithic.domain.admin.dto.storeapp.StoreApplicationSearchRequest;
import com.commerce.monolithic.domain.admin.dto.storeapp.StoreApplicationSummary;
import com.commerce.monolithic.domain.admin.entity.Admin;
import com.commerce.monolithic.domain.admin.entity.StoreApplication;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StoreApplicationService {

	private final StoreApplicationBase storeApplicationBase;
	private final AdminBase adminBase;

	@Transactional(readOnly = true)
	public StoreApplicationPageResponse list(StoreApplicationSearchRequest req) {
		StoreStatus status = req.getStatus();
		LocalDate from = req.getFrom();
		LocalDate to = req.getTo();
		int page = req.getPage() == null ? 0 : req.getPage();
		int size = req.getSize() == null ? 20 : req.getSize();

		List<StoreApplication> all = storeApplicationBase.findAll();

		LocalDateTime start = from != null ? from.atStartOfDay() : null;
		LocalDateTime end = to != null ? to.atTime(LocalTime.MAX) : null;

		var stream = all.stream();

		if (status != null) {
			stream = stream.filter(a -> statusEquals(a, status));
		}
		if (start != null) {
			stream = stream.filter(a -> a.getCreatedAt() == null || !a.getCreatedAt().isBefore(start));
		}
		if (end != null) {
			stream = stream.filter(a -> a.getCreatedAt() == null || !a.getCreatedAt().isAfter(end));
		}

		List<StoreApplication> filtered = stream
			.sorted(Comparator.comparing(StoreApplication::getCreatedAt,
				Comparator.nullsLast(Comparator.naturalOrder())).reversed())
			.toList();

		int total = filtered.size();
		int fromIdx = Math.max(page * size, 0);
		int toIdx = Math.min(fromIdx + size, total);
		List<StoreApplication> slice = (fromIdx < toIdx) ? filtered.subList(fromIdx, toIdx) : List.of();

		List<StoreApplicationSummary> items = slice.stream()
			.map(StoreApplicationSummary::from)
			.toList();

		return StoreApplicationPageResponse.builder()
			.items(items)
			.page(page)
			.size(size)
			.total(total)
			.build();
	}

	@Transactional(readOnly = true)
	public StoreApplicationDetailResponse detail(UUID id) {
		StoreApplication app = storeApplicationBase.getStoreApplication(id);
		return StoreApplicationDetailResponse.from(app);
	}

	@Transactional
	public void approve(UUID adminId, UUID id, StoreApplicationApproveRequest req) {
		StoreApplication app = storeApplicationBase.getStoreApplicationAssertNotProcessed(id);

		Admin reviewer = adminBase.getAdmin(adminId);
		app.setStatus(StoreStatus.APPROVED);
		app.setReviewedBy(reviewer);
		app.setReviewedAt(LocalDateTime.now());
		if (req != null && req.getNote() != null && !req.getNote().isBlank()) {
			app.setReason(req.getNote());
		}
	}

	@Transactional
	public void reject(UUID adminId, UUID id, StoreApplicationRejectRequest req) {
		StoreApplication app = storeApplicationBase.getStoreApplicationAssertNotProcessed(id);

		Admin reviewer = adminBase.getAdmin(adminId);
		app.setStatus(StoreStatus.REJECTED);
		app.setReviewedBy(reviewer);
		app.setReviewedAt(LocalDateTime.now());
		if (req != null && req.getReason() != null && !req.getReason().isBlank()) {
			app.setReason(req.getReason());
		}
	}

	private boolean statusEquals(StoreApplication app, StoreStatus target) {
		return readStatus(app) == target;
	}

	private StoreStatus readStatus(StoreApplication app) {
		Object raw = app.getStatus();
		if (raw instanceof StoreStatus ss)
			return ss;
		if (raw instanceof String str)
			return StoreStatus.valueOf(str);
		return StoreStatus.PENDING;
	}

}
