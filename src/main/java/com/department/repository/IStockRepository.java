package com.department.repository;

import com.department.entity.Department;
import com.department.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IStockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findOneBySymbol(String symbol);

    Optional<Stock> findOneByDocument(String document);

}
