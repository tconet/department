package com.department.utils;

import com.department.dto.DepartmentDTO;
import com.department.entity.Department;
import org.springframework.beans.BeanUtils;

public class ConvertUtils {

    public static Object fromToObject(Object from, Class toClazz) {
        Object dto = BeanUtils.instantiateClass(toClazz);
        BeanUtils.copyProperties(from, dto);
        return dto;
    }
}
