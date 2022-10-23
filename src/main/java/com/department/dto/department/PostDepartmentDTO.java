package com.department.dto.department;

import com.department.dto.DtoConverter;
import com.department.entity.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDepartmentDTO extends DtoConverter<PostDepartmentDTO, Department> {

    @Schema(name = "name", required = true, defaultValue="Financial")
    @Size(min = 2, message = "field.size")
    @NotEmpty(message = "field.name.mandatory")
    private String name;
    @Schema(name = "address", defaultValue="First Street Avenue")
    private String address;
    @Schema(name = "code", required = true, defaultValue="00002")
    @NotEmpty(message = "field.code.mandatory")
    private String code;

}
