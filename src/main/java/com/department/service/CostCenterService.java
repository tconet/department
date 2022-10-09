package com.department.service;

import com.department.entity.CostCenter;
import com.department.entity.query.SearchRequest;
import com.department.entity.query.SearchSpecification;
import com.department.repository.ICostCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  This class represents the business layer of {@link com.department.entity.CostCenter}
 *  entity. Here we must ensure all rules specified to control a Const Center.
 *
 *  Because of the nature of the business, we'll not provide a full CRUD operation
 *  under this entity, all information about it will come from integration.
 */
@Service
@RequiredArgsConstructor
public class CostCenterService {

    private final ICostCenterRepository repository;


    /**
     * Generic search engine, thanks to:
     * https://blog.piinalpin.com/2022/04/searching-and-filtering-using-jpa-specification/
     * On the link above, we'll find the best generic implementation of search criteria
     * specification for JPA.
     *
     * {
     *     "filters": [
     *         {
     *             "key": "name",
     *             "operator": "LIKE",
     *             "field_type": "STRING",
     *             "value": "SUPORTE"
     *         }
     *     ],
     *     "sorts": [
     *         {
     *             "key": "name",
     *             "direction": "ASC"
     *         }
     *     ],
     *     "page": null,
     *     "size": 10
     * }
     *
     * @param request
     * @return
     */
    public Page<CostCenter> search(SearchRequest request) {
        SearchSpecification<CostCenter> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        return repository.findAll(specification, pageable);
    }
}
