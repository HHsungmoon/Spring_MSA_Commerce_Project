package com.commerce.monolithic.domain.orderpayship.entity;

import java.util.UUID;

import com.commerce.monolithic.autotime.Time;
import com.commerce.monolithic.autotime.UuidBinaryAttributeConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
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
@Table(name = "order_shipping_addresses")
public class OrderShippingAddress {

	@Id
	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "id", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "order_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_ordaddr_order"))
	private Order order;

	@Column(name = "recipient_name", length = 100, nullable = false)
	private String recipientName;

	@Column(name = "recipient_phone", length = 30, nullable = false)
	private String recipientPhone;

	@Column(name = "zipcode", length = 20, nullable = false)
	private String zipcode;

	@Column(name = "addr1", length = 255, nullable = false)
	private String addr1;

	@Column(name = "addr2", length = 255)
	private String addr2;

	@Column(name = "city", length = 100)
	private String city;

	@Column(name = "state", length = 100)
	private String state;

	@Column(name = "country_code", length = 2, nullable = false)
	private String countryCode;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "p_time_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_ordaddr_p_time"))
	private Time timeRef;
}
