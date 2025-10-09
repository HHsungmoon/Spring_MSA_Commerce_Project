package com.commerce.monolithic.domain.orderpayship.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.commerce.monolithic.autotime.Time;
import com.commerce.monolithic.autotime.UuidBinaryAttributeConverter;
import com.commerce.monolithic.domain.catalogstore.entity.Product;
import com.commerce.monolithic.domain.catalogstore.entity.ProductVariant;

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
@Table(name = "order_items")
public class OrderItem {

	@Id
	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "order_item_id", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "order_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_order_items_order"))
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_order_items_product"))
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "variant_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_order_items_variant"))
	private ProductVariant variant;

	@Column(name = "product_name_snapshot", length = 255, nullable = false)
	private String productNameSnapshot;

	@Column(name = "sku_snapshot", length = 100, nullable = false)
	private String skuSnapshot;

	@Column(name = "unit_price_snapshot", precision = 18, scale = 2, nullable = false)
	private BigDecimal unitPriceSnapshot;

	@Column(name = "qty", nullable = false)
	private Integer qty;

	@Column(name = "line_amount", precision = 18, scale = 2, nullable = false)
	private BigDecimal lineAmount;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "p_time_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_order_items_p_time"))
	private Time timeRef;
}
