package com.department.dto.department;

import com.department.dto.DtoConverter;
import com.department.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDepartmentDTO extends DtoConverter<PostDepartmentDTO, Department> {

    @Size(min = 2, message = "field.size")
    @NotEmpty(message = "field.name.mandatory")
    private String name;
    private String address;
    @NotEmpty(message = "field.code.mandatory")
    private String code;

}
