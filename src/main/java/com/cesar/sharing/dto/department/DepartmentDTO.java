package com.cesar.sharing.dto.department;

import com.cesar.sharing.entity.Department;
import com.cesar.sharing.dto.DtoConverter;

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
