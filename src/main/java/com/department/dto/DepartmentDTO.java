package com.department.dto;

import com.department.entity.Department;
import com.department.utils.ConvertUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {

    private Long id;
    private String name;
    private String address;
    private String code;

    public static DepartmentDTO toDTO(Department entity) {
        DepartmentDTO dto = (DepartmentDTO)ConvertUtils.fromToObject(entity, DepartmentDTO.class);
        return dto;
    }

    public static List<DepartmentDTO> toDTOs(List<Department> entities) {
        List<DepartmentDTO> dtos = new ArrayList<>();
        for (Department entity: entities) {
            dtos.add(toDTO(entity));
        }
        return dtos;
    }
}
