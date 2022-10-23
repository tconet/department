package com.department.controller;

import com.department.dto.costcenterresource.CostcenterResourceFilterDTO;
import com.department.exceptions.BusinessException;
import com.department.model.SimpleCostCenter;
import com.department.service.CostcenterResourceService;
import com.department.types.CostCenterStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequiredArgsConstructor
@Tag(name = "Cost Center x Resource (Centro de Custo x Recurso)", description = "The Cost Center x Resource REST API with all available services")
@RequestMapping("costCenterResource")
public class CostcenterResourceController {

    private final CostcenterResourceService service;

    @Operation(summary = "Generic search for list Cost Center based filter object")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "302",
                    description = "found cost centers",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SimpleCostCenter.class)))})
    })
    @PostMapping("/search")
    public ResponseEntity search(@RequestBody CostcenterResourceFilterDTO filter) {

        String vStatus = filter.getStatus().trim().toLowerCase();
        if (!CostCenterStatus.isValid(vStatus))
            throw new BusinessException("costcenter.invalid.status", vStatus);

        Optional<List> entities = service.
                findAllCostCenterByApprover(
                        filter.getEmail(), filter.getCode(),
                        filter.getName(), vStatus, filter.isApprover());

        if (entities.isEmpty())
            entities = Optional.of(new ArrayList());
        return new ResponseEntity<>(entities.get(), HttpStatus.FOUND);
    }
}

