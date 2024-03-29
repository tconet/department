package com.cesar.sharing.dto.period;

import com.cesar.sharing.dto.DtoConverter;
import com.cesar.sharing.entity.Period;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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
public class OpenPeriodDTO extends DtoConverter<OpenPeriodDTO, Period> {

    @NotNull(message = "period.field.init.date.mandatory")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate initDate;

    @NotNull(message = "period.field.end.date.mandatory")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "UTC")
    private LocalTime endTime;

}
