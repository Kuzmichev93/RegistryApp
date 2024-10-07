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

public class Delete {

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
    public void deptNotExist(){
        Department department = new Department(1,"№1Tgg","g");
        Mockito.when(departmentRepository.existsByDeptname(department.getDeptname())).thenReturn(false);

        var excp = Assertions.assertThrows(СustomException.class,()->departmentService.delete(department));
        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());
        Assertions.assertEquals("Пользователь не существует в бд",excp.getMessage());
    }

    @Test
    public void deptExists() throws СustomException, JSONException {
        Department department = new Department(1,"№1Tgg","g");
        Mockito.when(departmentRepository.existsByDeptname(department.getDeptname())).thenReturn(true);
        Mockito.doNothing().when(departmentRepository).deleteByDeptname(department.getDeptname());

        var data = departmentService.delete(department);
        JSONObject jsonObject = new JSONObject(data);
        Assertions.assertNotNull(data);
        Assertions.assertEquals("Пользователь удален",jsonObject.get("Message"));

    }
}
