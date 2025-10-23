package com.commerce.monolithic.domain.admin.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;

import com.commerce.monolithic.autotime.BaseTimeEntity;
import com.commerce.monolithic.autotime.UuidBinaryAttributeConverter;
import com.commerce.monolithic.configenum.GlobalEnum.StoreStatus;
import com.commerce.monolithic.domain.store.entity.Manager;
import com.commerce.monolithic.domain.store.entity.Store;
import com.github.f4b6a3.uuid.UuidCreator;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(of = "applicationId", callSuper = false)
@Entity
@SQLRestriction("deleted_at IS NULL")
@Table(name = "store_applications")
public class StoreApplication extends BaseTimeEntity {

	@Id
	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "application_id", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "manager_id", nullable = false, foreignKey = @ForeignKey(name = "fk_storeapps_manager"))
	private Manager manager;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", foreignKey = @ForeignKey(name = "fk_storeapps_store"))
	private Store store;

	@Column(name = "description", length = 1000)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 16, nullable = false)
	private StoreStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reviewed_by", foreignKey = @ForeignKey(name = "fk_storeapps_admin"))
	private Admin reviewedBy;

	/** 반려/메모 사유 */
	@Column(name = "reason", length = 500)
	private String reason;

	@Column(name = "requested_at", nullable = false)
	private LocalDateTime requestedAt;

	@Column(name = "reviewed_at")
	private LocalDateTime reviewedAt;

	@PrePersist
	private void setIdIfNull() {
		if (this.id == null) {
			this.id = UuidCreator.getTimeOrderedEpoch();
		}
	}
}

