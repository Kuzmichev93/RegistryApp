package app.repository;

import app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer>{
     List<User> findByNameAndPatronomicAndSurnameOrNumberOrDepartmentFk(String name, String patronomic, String surname, String number, String department);
     void deleteByNumber(String number);
     boolean existsByNumber(String number);
     User findByNumber(String number);
}
