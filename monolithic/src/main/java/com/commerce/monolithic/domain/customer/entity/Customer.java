package com.commerce.monolithic.domain.customer.entity;

import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;

import com.commerce.monolithic.autotime.BaseTimeEntity;
import com.commerce.monolithic.autotime.UuidBinaryAttributeConverter;
import com.commerce.monolithic.configenum.GlobalEnum;
import com.github.f4b6a3.uuid.UuidCreator;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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
@Table(name = "p_customer")
public class Customer extends BaseTimeEntity {

	@Id
	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "id", columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "nickname", length = 100)
	private String nickname;

	@Column(name = "email", length = 255, nullable = false)
	private String email;

	@Column(name = "password", length = 255, nullable = false)
	private String password;

	@Column(name = "phone_number", length = 18)
	private String phoneNumber;

	@Column(name = "age")
	private Short age;

	@Column(name = "address", length = 255, nullable = false)
	private String address;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender", length = 16, nullable = false)
	private GlobalEnum.Gender gender;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 16, nullable = false)
	private GlobalEnum.CustomerStatus status;

	@PrePersist
	private void setIdIfNull() {
		if (this.id == null) {
			this.id = UuidCreator.getTimeOrderedEpoch();
		}
		if (this.gender == null) {
			this.gender = GlobalEnum.Gender.UNKNOWN;
		}
		if (this.status == null) {
			this.status = GlobalEnum.CustomerStatus.ACTIVE;
		}
	}
}
