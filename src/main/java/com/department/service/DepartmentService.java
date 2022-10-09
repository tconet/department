package com.department.service;

import com.department.entity.Department;
import com.department.entity.query.SearchRequest;
import com.department.entity.query.SearchSpecification;
import com.department.exceptions.BusinessException;
import com.department.repository.IDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Dependency Injection Best Practices
 * Best practices for dependency injection is to utilize interfaces, constructors, and final properties.
 *
 * --- Using Project Lombok ---
 * Now, the secret sauce using Project Lombok for best practices in dependency injection is to:
 * -- declare a final property of the interface type
 * -- annotate the class using Project Lombok’s required args constructor
 *
 * Now, Project Lombok will generate a constructor for all properties declared final. And Spring will automatically
 * use the Lombok provided constructor to autowire the clase.
 */
@Service
@RequiredArgsConstructor
public class DepartmentService {

    // Example of Dependency Injection and Inversion of Control
    /** The department repository */
    private final IDepartmentRepository repository;

    /**
     * <p>
     *  Saves the department, but before that, we must validate
     *  all business rules @see (@link validateBeforeSave())
     * @param entity {@link Department}
     * @return The new {@link Department} with id
     */
    @Transactional
    public Department save(Department entity) {
        validateBeforeSave(entity);
        return repository.save(entity);
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
    public Page<Department> search(SearchRequest request) {
        SearchSpecification<Department> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        return repository.findAll(specification, pageable);
    }

    /**
     * <p>
     *  List all departments
     * @return the list of {@link Department}
     */
    public List<Department> listAll() {
        return repository.findAll();
    }

    /**
     * <p>
     *  This function is intended to apply all business rules
     *  before saves a department object. For now, we only
     *  have one rule to validate, in this case, we must check
     *  if does not have another department with the same code
     * @param entity @see {@link Department}
     */
    private void validateBeforeSave(Department entity) {
        if ( repository.findOneByCode(entity.getCode()).isPresent() ) {
            throw new BusinessException("field.code.duplicated", entity.getCode());
        }
    }
}
