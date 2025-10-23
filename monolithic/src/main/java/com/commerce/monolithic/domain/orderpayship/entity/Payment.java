package com.commerce.monolithic.domain.orderpayship.entity;

import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;

import com.commerce.monolithic.autotime.BaseTimeEntity;
import com.commerce.monolithic.autotime.UuidBinaryAttributeConverter;
import com.commerce.monolithic.configenum.GlobalEnum.PaymentMethod;
import com.commerce.monolithic.configenum.GlobalEnum.PaymentStatus;
import com.commerce.monolithic.domain.customer.entity.Customer;
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
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@SQLRestriction("deleted_at IS NULL")
@Table(name = "p_payments")
public class Payment extends BaseTimeEntity {

	@Id
	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "payment_id", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "order_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_payments_order"))
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_payments_customer"))
	private Customer customer;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_status", nullable = false)
	private PaymentStatus paymentStatus = PaymentStatus.PENDING;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method", nullable = false)
	private PaymentMethod paymentMethod = PaymentMethod.KAKAOPAY;

	// 금액 INT
	@Column(name = "paid_amount")
	private Integer paidAmount;

	@Column(name = "pg_transaction_id", length = 120)
	private String pgTransactionId;

	@Column(name = "approval_code", length = 60)
	private String approvalCode;

	// JSON → 문자열 매핑 (필요 시 JsonType 사용 가능)
	@Column(name = "payload_json", columnDefinition = "json")
	private String payloadJson;

	@Column(name = "receipt_url", length = 512)
	private String receiptUrl;

	@Column(name = "failure_reason", length = 255)
	private String failureReason;

	@PrePersist
	private void setIdIfNull() {
		if (this.id == null) {
			this.id = UuidCreator.getTimeOrderedEpoch();
		}
	}
}
