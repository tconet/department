package com.department.service;

import com.department.entity.Department;
import com.department.exceptions.BusinessException;
import com.department.repository.IDepartmentRepository;
import lombok.RequiredArgsConstructor;
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
 *
 */
@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final IDepartmentRepository repository;

    @Transactional
    public Department save(Department entity) {
        if ( repository.findOneByCode(entity.getCode()).isPresent() ) {
            throw new BusinessException("field.code.duplicated", entity.getCode());
        }
        return repository.save(entity);
    }

    public List<Department> listAll() {
        return repository.findAll();
    }

}
