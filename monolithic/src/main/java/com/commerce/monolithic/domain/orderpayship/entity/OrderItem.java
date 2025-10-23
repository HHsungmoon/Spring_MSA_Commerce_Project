package com.commerce.monolithic.domain.orderpayship.entity;

import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;

import com.commerce.monolithic.autotime.BaseTimeEntity;
import com.commerce.monolithic.autotime.UuidBinaryAttributeConverter;
import com.commerce.monolithic.domain.store.entity.Product;
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
@Table(name = "order_items")
public class OrderItem extends BaseTimeEntity {

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

	// 스냅샷
	@Column(name = "product_name_snapshot", length = 255, nullable = false)
	private String productNameSnapshot;

	@Column(name = "sku_snapshot", length = 100, nullable = false)
	private String skuSnapshot;

	// 금액 INT
	@Column(name = "unit_price_snapshot", nullable = false)
	private Integer unitPriceSnapshot;

	@Column(name = "qty", nullable = false)
	private Integer qty;

	@Column(name = "line_amount", nullable = false)
	private Integer lineAmount;

	@PrePersist
	private void setIdIfNull() {
		if (this.id == null) {
			this.id = UuidCreator.getTimeOrderedEpoch();
		}
	}
}
