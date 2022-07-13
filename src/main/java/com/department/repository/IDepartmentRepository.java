package com.department.repository;

import com.department.entity.Department;
import org.hibernate.engine.jdbc.spi.ConnectionObserverAdapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findOneByCode(String code);
}
