package com.department.dto.stock;

import com.department.dto.DtoConverter;
import com.department.entity.Stock;
import com.department.types.StockType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDTO extends DtoConverter<StockDTO, Stock> {

    private Long id;
    private String symbol;
    private String companyName;
    private String document;
    private String description;
    private String image;
    private StockType type;

}
