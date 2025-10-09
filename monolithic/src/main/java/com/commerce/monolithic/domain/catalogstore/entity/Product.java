package com.commerce.monolithic.domain.catalogstore.entity;

import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;

import com.commerce.monolithic.autotime.BaseTimeEntity;
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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
@Table(name = "products")
public class Product extends BaseTimeEntity {

	@Id
	@Convert(converter = UuidBinaryAttributeConverter.class)
	@Column(name = "id", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "store_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_products_store"))
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "small_category_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_products_smallcat"))
	private SmallCategory smallCategory;

	@Column(name = "name", length = 255, nullable = false)
	private String name;

	@Column(name = "slug", length = 280, nullable = false)
	private String slug; // SEO/URL 식별자

	@Column(name = "summary", length = 500)
	private String summary;

	@Lob
	@Column(name = "description")
	private String description;

	@Column(name = "image_url", length = 1024, nullable = false)
	private String imageUrl;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 16, nullable = false)
	private GlobalEnum.ProductStatus status; // DRAFT/ACTIVE/INACTIVE
}
