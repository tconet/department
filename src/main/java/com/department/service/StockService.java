package com.department.service;

import com.department.entity.Stock;
import com.department.exceptions.BusinessException;
import com.department.repository.IStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final IStockRepository repository;

    /**
     * Saves the Stock entity into database. Before that, we have
     * to validate some business rules @see validation() method
     * on this class.
     * @param entity @see Stock
     * @return the new Stock entity with full fields.
     */
    @Transactional
    public Stock save(Stock entity) {

        validations(entity);

        return repository.save(entity);
    }

    /**
     * List all Stocks available.
     * @return List of @see Stock
     */
    public List<Stock> listAll() {
        return repository.findAll();
    }

    /**
     * Execute the validation process. For now, only check if
     * the Symbol or Document already exist. If true, an
     * exception will be thrown.
     * @param entity @see Stock
     */
    private void validations(Stock entity) {
        ArrayList<String> params;
        Stock duplicated;
        Optional<Stock> option = repository.findOneBySymbol(entity.getSymbol());
        if ( option.isPresent() ) {
            duplicated = option.get();
            params = new ArrayList<>();
            params.add("symbol");
            params.add(duplicated.getSymbol());
            throw new BusinessException("field.duplicated", params);
        }
        option = repository.findOneByDocument(entity.getDocument());
        if ( option.isPresent() ) {
            duplicated = option.get();
            params = new ArrayList<>();
            params.add("document");
            params.add(duplicated.getDocument());
            throw new BusinessException("field.duplicated", params);
        }
    }
}
