package app.unit.service.departmentservice;

import app.exception.СustomException;
import app.model.Department;
import app.repository.DepartmentRepository;
import app.repository.UserRepository;
import app.service.DepartmentService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class AddAll {
    @InjectMocks
    private DepartmentService departmentService;

    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private UserRepository userRepository;







    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void departmentsNotExist(){
        List<Department> departmentList = new ArrayList<>();

        var excp = Assertions.assertThrows(СustomException.class,()->departmentService.addAll(departmentList));
        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());
        Assertions.assertEquals("Должен быть передан список содержащий deptname и prefix",excp.getMessage());
    }

    @Test
    public void noOneArgument(){
        List<Department> departmentList = new ArrayList<>();
        departmentList.add(new Department(1,"№1gg","f"));
        departmentList.add(new Department(2,"№1gg",null));

        var excp = Assertions.assertThrows(СustomException.class,()->departmentService.addAll(departmentList));
        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());
        Assertions.assertEquals("Объект с индексом 2 должен содержать поля deptname и prefix",excp.getMessage());
    }

    @Test
    public void OneArgumentAndInvalidArguments(){
        List<Department> departmentList = new ArrayList<>();
        departmentList.add(new Department(1,"№1gg","f"));
        departmentList.add(new Department(2,"№1gg","7"));

        var excp = Assertions.assertThrows(СustomException.class,()->departmentService.addAll(departmentList));
        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());
        Assertions.assertEquals("Объект с индексом 2 содержит невалидные значения",excp.getMessage());
    }

    @Test
    public void saveList() throws JSONException, СustomException {
        List<Department> departmentList = new ArrayList<>();
        departmentList.add(new Department(1,"№1gg","f"));
        departmentList.add(new Department(2,"№1gg","h"));
        Mockito.when(departmentRepository.saveAll(departmentList)).thenReturn(departmentList);

        var data = departmentService.addAll(departmentList);
        JSONObject jsonObject = new JSONObject();
        Assertions.assertEquals("Департаменты добавлены в бд",jsonObject.get("Message"));
    }
}
