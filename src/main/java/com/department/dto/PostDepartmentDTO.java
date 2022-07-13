package com.department.dto;

import com.department.entity.Department;
import com.department.utils.ConvertUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDepartmentDTO {

    @Size(min = 2, message = "field.size")
    @NotEmpty(message = "field.name.mandatory")
    private String name;
    private String address;
    @NotEmpty(message = "field.code.mandatory")
    private String code;

    public Department toEntity() {
        Department entity = (Department) ConvertUtils.fromToObject(this, Department.class);
        return entity;
    }

}
