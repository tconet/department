package com.department.controller;

import com.department.dto.DepartmentDTO;
import com.department.dto.PostDepartmentDTO;
import com.department.entity.Department;
import com.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class DepartmentController {

    private final ModelMapper mapper;
    private final DepartmentService service;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<DepartmentDTO> save(@Valid @RequestBody PostDepartmentDTO dto) {

        log.info("Saving department...");
        Department entity = dto.toEntity();
        entity = service.save(entity);
        return new ResponseEntity<>(DepartmentDTO.toDTO(entity), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DepartmentDTO>> findAll() {

        List<Department> entities = service.listAll();
        log.info("Listing all departments...");
        List<DepartmentDTO> dtos = DepartmentDTO.toDTOs(entities);
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

}
