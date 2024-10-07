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

public class Add {

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
    public void twoArgumentAndValidArguments() throws JSONException {
        Department department = new Department(1,"№1Test","j");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",department.getId());
        jsonObject.put("deptname",department.getDeptname());
        jsonObject.put("prefix",department.getPrefix());
        Mockito.when(departmentRepository.save(department)).thenReturn(department);

        Assertions.assertDoesNotThrow(()->departmentService.add(department));

    }

    @Test
    public void twoArgumentAndInvalidArguments(){
        Department department = new Department(1,"№Test","j");

        var excp = Assertions.assertThrows(СustomException.class,()->departmentService.add(department));
        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());
        Assertions.assertEquals("Объект содержит невалидные данные",excp.getMessage());

    }
    @Test
    public void oneArgument(){
        Department department = new Department(1,"test",null);
        var excp = Assertions.assertThrows(СustomException.class,()->departmentService.add(department));

        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());
        Assertions.assertEquals("Поле deptname и prefix должны быть в запросе",excp.getMessage());
    }

    @Test
    public void save() throws JSONException, СustomException {
        Department department = new Department(1,"№1Test","j");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",department.getId());
        jsonObject.put("deptname",department.getDeptname());
        jsonObject.put("prefix",department.getPrefix());
        Mockito.when(departmentRepository.save(department)).thenReturn(department);

        var data = departmentService.add(department);
        JSONObject value = new JSONObject(data);
        Assertions.assertNotNull(value);
        Assertions.assertEquals(1,value.get("id"));
        Assertions.assertEquals("№1Test",value.get("deptname"));
        Assertions.assertEquals("j",value.get("prefix"));
    }
}
