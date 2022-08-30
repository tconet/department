package com.department.controller;

import com.department.dto.stock.PostStockDTO;
import com.department.dto.stock.StockDTO;
import com.department.entity.Stock;
import com.department.service.StockService;
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
@RequestMapping("stock")
public class StockController {

    private final StockService service;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<StockDTO> save(@Valid @RequestBody PostStockDTO dto) {
        Stock entity = dto.toBusiness(Stock.class);
        entity = service.save(entity);
        return new ResponseEntity<>( new StockDTO().toDTO(entity, StockDTO.class), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<StockDTO>> findAll() {

        List<Stock> entities = service.listAll();
        List<StockDTO> dtos = new StockDTO().toDTOs(entities, StockDTO.class);
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

}
