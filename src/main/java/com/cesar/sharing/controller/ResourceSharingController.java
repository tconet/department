package com.cesar.sharing.controller;

import com.cesar.sharing.dto.resourcesharing.CreateResourceSharingDTO;
import com.cesar.sharing.entity.ResourceSharing;
import com.cesar.sharing.service.ResourceSharingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Resouce Sharing (Rateio)", description = "The Resource Sharing REST API with all available services")
@RequestMapping("resource-sharing")
public class ResourceSharingController {

    private final ResourceSharingService service;

    @Operation(summary = "Sharing the Resource")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Resource Shared",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreateResourceSharingDTO.class)) }),
            @ApiResponse(responseCode = "500", description = "Business exception", content = @Content)})
    @PostMapping("/share")
    @ResponseBody
    public ResponseEntity<String> share(
            @Parameter(description = "List of resource sharing")
            @Valid @RequestBody List<CreateResourceSharingDTO> dtos) {

        List<ResourceSharing> entities = new CreateResourceSharingDTO().toBusiness(dtos);
        entities = service.share(entities);
        return new ResponseEntity<>( "", HttpStatus.CREATED);
    }
}
