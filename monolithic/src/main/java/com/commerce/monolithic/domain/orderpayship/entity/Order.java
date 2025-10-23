package com.commerce.monolithic.domain.orderpayship.entity;

import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;

import com.commerce.monolithic.autotime.BaseTimeEntity;
import com.commerce.monolithic.autotime.UuidBinaryAttributeConverter;
import com.commerce.monolithic.configenum.GlobalEnum.OrderStatus;
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
@Table(name = "p_orders")
public class Order extends BaseTimeEntity {

	@Id
	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "order_id", columnDefinition = "BINARY(16)", nullable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_orders_customer"))
	private Customer customer;

	@Column(name = "order_number", length = 60, nullable = false, unique = true)
	private String orderNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status", nullable = false)
	private OrderStatus orderStatus = OrderStatus.PENDING;

	// 금액: KRW "원" 단위 정수
	@Column(name = "subtotal_amount", nullable = false)
	private Integer subtotalAmount = 0;

	@Column(name = "shipping_fee", nullable = false)
	private Integer shippingFee = 0;

	@Column(name = "discount_amount", nullable = false)
	private Integer discountAmount = 0;

	@Column(name = "final_payment_amount", nullable = false)
	private Integer finalPaymentAmount;

	// 배송지 스냅샷
	@Column(name = "recipient_name", length = 100, nullable = false)
	private String recipientName;

	@Column(name = "recipient_phone", length = 30, nullable = false)
	private String recipientPhone;

	@Column(name = "address", length = 255, nullable = false)
	private String address;

	@PrePersist
	private void setIdIfNull() {
		if (this.id == null) {
			this.id = UuidCreator.getTimeOrderedEpoch();
		}
	}
}
