package app.repository;

import app.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    boolean existsByDeptname(String deptname);
    Optional<Department> findByDeptname(String deptname);
    void deleteByDeptname(String deptname);
}
