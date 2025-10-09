package com.commerce.monolithic.domain.catalogstore.entity;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.commerce.monolithic.autotime.Time;
import com.commerce.monolithic.autotime.UuidBinaryAttributeConverter;
import com.commerce.monolithic.configenum.GlobalEnum;

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
@Table(name = "product_variants")
public class ProductVariant {

	@Id
	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "id", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_variants_product"))
	private Product product;

	@Column(name = "sku", length = 100, nullable = false)
	private String sku;

	@Column(name = "currency", length = 3, nullable = false)
	private String currency = "KRW";

	@Column(name = "price", precision = 18, scale = 2, nullable = false)
	private BigDecimal price;

	@Column(name = "list_price", precision = 18, scale = 2)
	private BigDecimal listPrice;

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "attributes_json", columnDefinition = "JSON")
	private String attributesJson; // 의류/식품 등 속성 임시

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 16, nullable = false)
	private GlobalEnum.VariantStatus status; // ACTIVE/INACTIVE

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "p_time_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_variants_p_time"))
	private Time timeRef;
}
