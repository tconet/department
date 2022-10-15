package com.department.controller;

import com.department.exceptions.BusinessException;
import com.department.model.SimpleCostCenter;
import com.department.service.CostcenterResourceService;
import com.department.types.CostCenterStatus;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequiredArgsConstructor
@Tag(name = "Cost Center x Resource (Centro de Custo x Recurso)", description = "The Cost Center x Resource REST API with all available services")
@RequestMapping("costCenterResource")
public class CostcenterResourceController {

    private final CostcenterResourceService service;

    @Operation(summary = "List all Cost Center by Approver. We must provide the approver's email")
    @Parameter(name = "email", description = "approver's email", allowEmptyValue = false)
    @Parameter(name = "status", description = "cost center status", allowEmptyValue = false, array = @ArraySchema(schema = @Schema(implementation = CostCenterStatus.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "found cost center relationships for the informed approver", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleCostCenter.class)))}),
            @ApiResponse(responseCode = "404", description = "No Cost Center found based on approver's email", content = @Content)})
    @GetMapping("/findByEmail")
    public ResponseEntity<List<SimpleCostCenter>> findByEmail(@RequestParam String email, @RequestParam String status) {

        String vStatus = status.trim().toLowerCase();
        if (!CostCenterStatus.isValid(vStatus))
            throw new BusinessException("costcenter.invalid.status", vStatus);

        Optional<List<SimpleCostCenter>> entities = service.findAllCostCenterByApprover(email, vStatus);
        return new ResponseEntity<>(entities.get(), HttpStatus.FOUND);
    }
}

