package com.department.service;

import com.department.entity.Department;
import com.department.exceptions.BusinessException;
import com.department.repository.IDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
