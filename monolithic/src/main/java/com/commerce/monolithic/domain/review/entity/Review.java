package com.commerce.monolithic.domain.review.entity;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.commerce.monolithic.autotime.Time;
import com.commerce.monolithic.autotime.UuidBinaryAttributeConverter;
import com.commerce.monolithic.domain.catalogstore.entity.Product;
import com.commerce.monolithic.domain.catalogstore.entity.ProductVariant;
import com.commerce.monolithic.domain.customer.entity.Customer;
import com.commerce.monolithic.domain.orderpayship.entity.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
@Table(name = "p_reviews")
public class Review {

	@Id
	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "review_id", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "order_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_reviews_order"))
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_reviews_customer"))
	private Customer customer;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_reviews_product"))
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "variant_id",
		foreignKey = @ForeignKey(name = "fk_reviews_variant"))
	private ProductVariant variant; // nullable

	@Column(name = "rating", precision = 2, scale = 1, nullable = false)
	private BigDecimal rating;      // 0.0 ~ 5.0

	@Lob
	@Column(name = "content")
	private String content;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "images", columnDefinition = "JSON")
	private String images;          // ["url1","url2",...]

	@Column(name = "is_public", nullable = false)
	private boolean isPublic = true;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "p_time_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_reviews_p_time"))
	private Time timeRef;
}
