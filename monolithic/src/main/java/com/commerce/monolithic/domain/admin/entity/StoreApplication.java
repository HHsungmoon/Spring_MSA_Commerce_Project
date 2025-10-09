package com.commerce.monolithic.domain.admin.entity;

import java.time.Instant;
import java.util.UUID;

import com.commerce.monolithic.autotime.Time;
import com.commerce.monolithic.autotime.UuidBinaryAttributeConverter;
import com.commerce.monolithic.configenum.GlobalEnum.StoreStatus;
import com.commerce.monolithic.domain.catalogstore.entity.Manager;
import com.commerce.monolithic.domain.catalogstore.entity.Store;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "applicationId")
@Entity
@Table(name = "store_applications")
public class StoreApplication {

	@Id
	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "application_id", columnDefinition = "BINARY(16)")
	private UUID applicationId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "manager_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_storeapps_manager"))
	private Manager manager;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id",
		foreignKey = @ForeignKey(name = "fk_storeapps_store"))
	private Store store;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 16, nullable = false)
	private StoreStatus status; // PENDING/APPROVED/REJECTED

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reviewed_by",
		foreignKey = @ForeignKey(name = "fk_storeapps_admin"))
	private Admin reviewedBy;

	/** 반려/메모 사유 */
	@Column(name = "reason", length = 500)
	private String reason;

	@Column(name = "requested_at", nullable = false)
	private Instant requestedAt;

	@Column(name = "reviewed_at")
	private Instant reviewedAt;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "p_time_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_storeapps_p_time"))
	private Time timeRef;
}

