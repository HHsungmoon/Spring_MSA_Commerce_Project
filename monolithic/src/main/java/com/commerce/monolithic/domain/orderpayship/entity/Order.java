package com.commerce.monolithic.domain.orderpayship.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.commerce.monolithic.autotime.Time;
import com.commerce.monolithic.autotime.UuidBinaryAttributeConverter;
import com.commerce.monolithic.configenum.GlobalEnum.OrderStatus;
import com.commerce.monolithic.domain.customer.entity.Customer;

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
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "p_orders")
public class Order {

	@Id
	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "order_id", columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "order_number", length = 60, nullable = false)
	private String orderNumber;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_orders_customer"))
	private Customer customer;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status", length = 16, nullable = false)
	private OrderStatus orderStatus;

	@Column(name = "order_type", length = 16, nullable = false)
	private String orderType = "GENERAL";

	@Column(name = "subtotal_amount", precision = 18, scale = 2, nullable = false)
	private BigDecimal subtotalAmount;

	@Column(name = "shipping_fee", precision = 18, scale = 2, nullable = false)
	private BigDecimal shippingFee;

	@Column(name = "discount_amount", precision = 18, scale = 2, nullable = false)
	private BigDecimal discountAmount;

	@Column(name = "final_payment_amount", precision = 18, scale = 2, nullable = false)
	private BigDecimal finalPaymentAmount;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "p_time_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_orders_p_time"))
	private Time timeRef;
}
