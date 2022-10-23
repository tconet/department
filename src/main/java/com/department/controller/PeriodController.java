package com.department.controller;

import com.department.dto.period.PeriodDTO;
import com.department.dto.period.PostPeriodDTO;
import com.department.dto.period.UpdatePeriodDTO;
import com.department.entity.Period;
import com.department.entity.query.SearchRequest;
import com.department.service.PeriodService;
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
@Tag(name = "Period (Período)", description = "The Period REST API with all available services")
@RequestMapping("period")
public class PeriodController {

    private final PeriodService service;

    @Operation(summary = "Open a new Period")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Period Opened",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PeriodDTO.class)) }),
            @ApiResponse(responseCode = "500", description = "Business exception", content = @Content)})
    @PostMapping("/open")
    @ResponseBody
    public ResponseEntity<PeriodDTO> open(
            @Parameter(description = "period with all information to be opened")
            @Valid @RequestBody PostPeriodDTO dto) {

        Period entity = dto.toBusiness(Period.class);
        entity = service.open(entity);
        return new ResponseEntity<>( new PeriodDTO().toDTO(entity, PeriodDTO.class), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a Period end date")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Period Updated",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PeriodDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid end date supplied", content = @Content)})
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<PeriodDTO> update(
            @Parameter(description = "Period end date to be updated")
            @Valid @RequestBody UpdatePeriodDTO dto) {

        Period entity = service.update(dto.getEndDate(), dto.getEndTime());
        return new ResponseEntity<>( new PeriodDTO().toDTO(entity, PeriodDTO.class), HttpStatus.CREATED);
    }

    @Operation(summary = "Close the current Period")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Period close",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PeriodDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Does not exist an opened period", content = @Content)})
    @PostMapping("/close")
    @ResponseBody
    public ResponseEntity<PeriodDTO> close() {
        Period entity = service.close();
        return new ResponseEntity<>( new PeriodDTO().toDTO(entity, PeriodDTO.class), HttpStatus.CREATED);
    }

    @Operation(summary = "Generic search for cost resource entity")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "302",
                    description = "found cost centers",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PeriodDTO.class)))})
    })
    @PostMapping(value = "/search")
    public ResponseEntity<Page<PeriodDTO>> search(@RequestBody SearchRequest request) {
        Page<Period> entities = service.search(request);
        List<PeriodDTO> dtos = new PeriodDTO().toDTOs(entities.getContent(), PeriodDTO.class);
        Page<PeriodDTO> page = new PageImpl<>(dtos, entities.getPageable(), entities.getTotalElements());
        return new ResponseEntity<>(page, HttpStatus.FOUND);
    }

}
