package com.department.controller;

import com.department.dto.department.DepartmentDTO;
import com.department.dto.department.PostDepartmentDTO;
import com.department.entity.Department;
import com.department.entity.query.SearchRequest;
import com.department.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@Tag(name = "Department", description = "the department API with documentation annotations")
public class DepartmentController {

    private final DepartmentService service;

    @Operation(summary = "Create a new Department")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Department Created",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DepartmentDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content)})
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<DepartmentDTO> save(
            @Parameter(description = "department object to be created")
            @Valid @RequestBody PostDepartmentDTO dto) {

        Department entity = dto.toBusiness(Department.class);
        entity = service.save(entity);
        return new ResponseEntity<>( new DepartmentDTO().toDTO(entity, DepartmentDTO.class), HttpStatus.CREATED);
    }

    @Operation(summary = "List all Departments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "found departments", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DepartmentDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "No Department found", content = @Content) })
    @GetMapping("/all")
    public ResponseEntity<List<DepartmentDTO>> findAll() {

        List<Department> entities = service.listAll();
        List<DepartmentDTO> dtos = new DepartmentDTO().toDTOs(entities, DepartmentDTO.class);
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @PostMapping(value = "/search")
    public ResponseEntity<Page> search(@RequestBody SearchRequest request) {

        Page<Department> entities = service.search(request);
        List<DepartmentDTO> dtos = new DepartmentDTO().toDTOs(entities.getContent(), DepartmentDTO.class);
        Page page = new PageImpl<DepartmentDTO>(dtos, entities.getPageable(), entities.getTotalElements());
        return new ResponseEntity<>(page, HttpStatus.FOUND);
    }
}
