package com.cesar.sharing.dto.period;

import com.cesar.sharing.dto.DtoConverter;
import com.cesar.sharing.entity.Period;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * <p>
 * Simple DTO (Data Transfer Object) that represents the business
 * entity {@link Period}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeriodDTO extends DtoConverter<PeriodDTO, Period> {

    private Long id;
    @Schema(name = "initDate")
    private LocalDate initDate;
    @Schema(name = "endDate")
    private LocalDate endDate;
    @Schema(name = "endTime")
    private LocalTime endTime;
    @Schema(name = "status", defaultValue="opened")
    private String status;
}
