package com.commerce.monolithic.domain.store.entity;

import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;

import com.commerce.monolithic.autotime.BaseTimeEntity;
import com.commerce.monolithic.autotime.UuidBinaryAttributeConverter;
import com.github.f4b6a3.uuid.UuidCreator;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
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
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@SQLRestriction("deleted_at IS NULL")
@Table(name = "small_categories")
public class SmallCategory extends BaseTimeEntity {

	@Id
	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "id", columnDefinition = "BINARY(16)", nullable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "big_id", nullable = false, foreignKey = @ForeignKey(name = "fk_smallcat_big"))
	private BigCategory bigCategory;

	@Column(name = "name", length = 120, nullable = false)
	private String name;

	@PrePersist
	private void setIdIfNull() {
		if (this.id == null) {
			this.id = UuidCreator.getTimeOrderedEpoch();
		}
	}
}
