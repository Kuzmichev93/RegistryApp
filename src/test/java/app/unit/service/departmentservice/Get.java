package app.unit.service.departmentservice;

import app.exception.СustomException;
import app.model.Department;
import app.repository.DepartmentRepository;
import app.service.DepartmentService;
import org.json.JSONArray;
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

public class Get {

    @InjectMocks
    private DepartmentService departmentService;

    @Mock
    private DepartmentRepository departmentRepositorys;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void departmentsExistsInBd() throws СustomException, JSONException {
        List<Department> departmentList = new ArrayList<>();
        departmentList.add(new Department(1,"test1","f"));
        departmentList.add(new Department(2,"test2","g"));
        Mockito.when(departmentRepositorys.findAll()).thenReturn(departmentList);

        var data = departmentService.get();
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        Assertions.assertNotNull(data);
        Assertions.assertEquals(2,data.length());
        Assertions.assertEquals("test1",jsonArray.getJSONObject(0).get("deptname"));
        Assertions.assertEquals("test2",jsonArray.getJSONObject(1).get("deptname"));
    }

    @Test
    public void departmentsNotExist() throws СustomException {
        List<Department> departmentList = new ArrayList<>();
        Mockito.when(departmentRepositorys.findAll()).thenReturn(departmentList);

        var excp = Assertions.assertThrows(СustomException.class,()->departmentService.get());
        Assertions.assertEquals("Департаменты в бд не найдены",excp.getMessage());
    }
}
