package com.department.controller;

import com.department.dto.costcenter.CostCenterDTO;
import com.department.entity.CostCenter;
import com.department.entity.query.SearchRequest;
import com.department.service.CostCenterService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@Tag(name = "Cost Center (Centro de Custo)", description = "The Cost Center REST API with all available services")
@RequestMapping("costCenter")
public class CostCenterController {

    private final CostCenterService service;

    @Operation(summary = "Generic search for cost center entity")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "302",
                    description = "found cost centers",
                    content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CostCenterDTO.class)))})
             })
    @PostMapping(value = "/search")
    public ResponseEntity<Page<CostCenterDTO>> search(@RequestBody SearchRequest request) {
        Page<CostCenter> entities = service.search(request);
        List<CostCenterDTO> dtos = new CostCenterDTO().toDTOs(entities.getContent(), CostCenterDTO.class);
        Page<CostCenterDTO> page = new PageImpl<>(dtos, entities.getPageable(), entities.getTotalElements());
        return new ResponseEntity<>(page, HttpStatus.FOUND);
    }

}
