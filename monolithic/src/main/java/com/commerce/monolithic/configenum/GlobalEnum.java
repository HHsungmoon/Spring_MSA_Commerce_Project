package com.commerce.monolithic.configenum;

public class GlobalEnum {
	public enum Gender {MALE, FEMALE, OTHER, UNKNOWN}

	public enum CustomerStatus {ACTIVE, INACTIVE, BANNED}

	public enum StoreStatus {PENDING, APPROVED, REJECTED}

	public enum ProductStatus {DRAFT, ACTIVE, INACTIVE}

	public enum VariantStatus {ACTIVE, INACTIVE}

	public enum CommonStatus {ACTIVE, INACTIVE, BANNED}

	public enum OrderStatus {PENDING, PAID, SHIPPING, DELIVERED, CANCELED, REFUNDED}

	public enum PaymentStatus {PENDING, PAID, CANCELED, REFUNDED}

	public enum PaymentMethod {KAKAOPAY, CARD, VA, NAVER_PAY, KAKAO_PAY, BANK_TRANSFER}

	public enum ShipmentStatus {READY, IN_TRANSIT, DELIVERED}

	public enum Vote {UP, DOWN}
}
