package com.commerce.monolithic.autotime;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "p_time")
public class Time {

	@Id
	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "p_time_id", columnDefinition = "BINARY(16)")
	@ToString.Include
	private UUID id;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	@Comment("생성 시각")
	private Instant createdAt;

	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "created_by", columnDefinition = "BINARY(16)")
	private UUID createdBy;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	@Comment("수정 시각")
	private Instant updatedAt;

	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "updated_by", columnDefinition = "BINARY(16)")
	private UUID updatedBy;

	@Column(name = "deleted_at")
	@Comment("소프트 삭제 시각 (NULL이면 활성)")
	private Instant deletedAt;

	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "deleted_by", columnDefinition = "BINARY(16)")
	private UUID deletedBy;

	public boolean isDeleted() {
		return deletedAt != null;
	}

	public void markDeleted(UUID actorId, Instant when) {
		this.deletedBy = actorId;
		this.deletedAt = when != null ? when : Instant.now();
	}
}
