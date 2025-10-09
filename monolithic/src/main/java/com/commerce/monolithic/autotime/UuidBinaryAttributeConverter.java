package com.commerce.monolithic.autotime;

import java.nio.ByteBuffer;
import java.util.UUID;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/** UUID <-> BINARY(16) (UUID v7 포함) */
@Converter(autoApply = true)
public class UuidBinaryAttributeConverter implements AttributeConverter<UUID, byte[]> {
	@Override
	public byte[] convertToDatabaseColumn(UUID attribute) {
		if (attribute == null)
			return null;
		return ByteBuffer.allocate(16)
			.putLong(attribute.getMostSignificantBits())
			.putLong(attribute.getLeastSignificantBits())
			.array();
	}

	@Override
	public UUID convertToEntityAttribute(byte[] dbData) {
		if (dbData == null)
			return null;
		var bb = ByteBuffer.wrap(dbData);
		return new UUID(bb.getLong(), bb.getLong());
	}
}
