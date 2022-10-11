package com.department.service;

import com.department.entity.CostCenter;
import com.department.entity.query.SearchRequest;
import com.department.entity.query.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 *  This is the generic (default) implementation of a generic search
 *  using {@link JpaSpecificationExecutor} pattern. So, each service
 *  layer that pretends to provide this kind of generic search should
 *  extends from this class to inherit this behaviour.
 * @param <B> Must be the child entity class type. Generally, one of
 *            those class that belongs the com.department.entity.* package
 */
public class SearchService<B> implements IService<B>  {

    private JpaSpecificationExecutor<B> repository;

    /**
     * <p>
     *  Default constructor, only exist explicitly to enable the child
     *  class provides the correct instance of the desired repository interface.
     * @param repository Ony implementation of {@link JpaSpecificationExecutor}
     */
    public SearchService(JpaSpecificationExecutor<B> repository) {
        this.repository = repository;
    }

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
     *             "operator": "NOT_EQUAL",
     *             "field_type": "STRING",
     *             "value": "CentOS"
     *         }
     *     ],
     *     "sorts": [
     *         {
     *             "key": "releaseDate",
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
    @Override
    public Page<B> search(SearchRequest request) {
        SearchSpecification<B> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        return repository.findAll(specification, pageable);

    }
}
