package com.department.dto.stock;

import com.department.dto.DtoConverter;
import com.department.entity.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


/**
 * DTO representation of Stock business entity. Only used to expose
 * all necessary information for create a NEW Stock. We are using
 * the validation annotation to ensure that all mandatory fields
 * will be present before send to service layer.
 */
@Data
@AllArgsConstructor
public class PostStockDTO extends DtoConverter<PostStockDTO, Stock> {

    @Size(min = 5, max = 5, message = "field.size")
    @NotEmpty(message = "field.symbol.mandatory")
    private String symbol;
    @NotEmpty(message = "field.company.name.mandatory")
    private String companyName;
    @NotEmpty(message = "field.document.mandatory")
    private String document;
    private String description;
    private String image;
    @NotEmpty(message = "field.stock.type.mandatory")
    private String type;


}
