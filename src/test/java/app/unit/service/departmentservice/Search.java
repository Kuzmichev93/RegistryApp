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

public class Search {

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
    public void argumentIsNull()  {
        Department department = new Department(1,null,"g");

        var excp = Assertions.assertThrows(СustomException.class,()->departmentService.search(department));
        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());
        Assertions.assertEquals("Поле deptname должно быть в запросе",excp.getMessage());

    }

    @Test
    public void invalidArguments(){
        Department department = new Department(1,"test","g");
        var excp = Assertions.assertThrows(СustomException.class,()->departmentService.search(department));
        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());
        Assertions.assertEquals("Объект содержит невалидные данные",excp.getMessage());
    }

    @Test
    public void search() throws СustomException, JSONException {
        Department department = new Department(1,"№1Test","g");
        Mockito.when(departmentRepository.findByDeptname(department.getDeptname())).thenReturn(java.util.Optional.of(department));

        var data = departmentService.search(department);
        JSONObject jsonObject = new JSONObject(data);

        Assertions.assertNotNull(data);
        Assertions.assertEquals(1,jsonObject.get("id"));
        Assertions.assertEquals("№1Test",jsonObject.get("deptname"));
        Assertions.assertEquals("g",jsonObject.get("prefix"));
    }
}
