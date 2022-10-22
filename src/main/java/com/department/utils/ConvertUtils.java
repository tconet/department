package com.department.utils;

import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ConvertUtils {

    public static Object fromToObject(Object from, Class toClazz) {
        Object dto = BeanUtils.instantiateClass(toClazz);
        BeanUtils.copyProperties(from, dto);
        return dto;
    }

    public static String toBrazilFormat(LocalDate date) {
        if (Objects.isNull(date))
            date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }
}
