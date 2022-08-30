package com.department.dto.department;

import com.department.dto.DtoConverter;
import com.department.entity.Department;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO extends DtoConverter<DepartmentDTO, Department> {

    private Long id;
    private String name;
    private String address;
    private String code;

}
