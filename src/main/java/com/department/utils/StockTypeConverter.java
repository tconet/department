package com.department.utils;

import com.department.types.StockType;

import javax.persistence.AttributeConverter;
import java.util.stream.Stream;

public class StockTypeConverter implements AttributeConverter<StockType, String> {

    @Override
    public String convertToDatabaseColumn(StockType type) {
        if (type == null) {
            return null;
        }
        return type.getCode();
    }

    @Override
    public StockType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(StockType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
