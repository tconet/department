package com.department.controller;

import com.department.dto.department.DepartmentDTO;
import com.department.dto.department.PostDepartmentDTO;
import com.department.entity.Department;
import com.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService service;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<DepartmentDTO> save(@Valid @RequestBody PostDepartmentDTO dto) {

        Department entity = dto.toBusiness(Department.class);
        entity = service.save(entity);
        return new ResponseEntity<>( new DepartmentDTO().toDTO(entity, DepartmentDTO.class), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DepartmentDTO>> findAll() {

        List<Department> entities = service.listAll();
        List<DepartmentDTO> dtos = new DepartmentDTO().toDTOs(entities, DepartmentDTO.class);
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

}
