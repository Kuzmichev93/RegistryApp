package app.unit.service.userservice;

import app.component.NumberUser;
import app.exception.СustomException;
import app.model.Department;
import app.model.User;
import app.repository.DepartmentRepository;
import app.repository.UserRepository;
import app.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Optional;

public class Add {
    @InjectMocks
    private UserService userService;

    @Mock
    private DepartmentRepository departmentRepositorys;
    @Mock
    private UserRepository userRepository;


    @Mock
    private NumberUser numberUser;




    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void sendAllArgumentsAreNull() throws JSONException, СustomException {
        User user = new User(null,null,null,null,null,null);
        var excp = Assertions.assertThrows(СustomException.class,()->{userService.add(user);});
        Assertions.assertEquals("Поле number,patronomic,surname и department не должны быть пустыми",excp.getMessage());
        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());



    }

    @Test
    public void idDeptNotExist(){
        User user = new User(1,"test","test","test","5","test 1");
        Mockito.when(departmentRepositorys.existsByDeptname(user.getDepartment())).thenReturn(false);
        var excp = Assertions.assertThrows(СustomException.class,()->{userService.add(user);});
        Assertions.assertEquals("Атрибут department содержит несуществующий id",excp.getMessage());
        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());

    }
    @Test
    public void idDeptExists() throws JSONException, СustomException {
        User user = new User(1,"test","test","test","5","test 1");
        Department department = new Department();
        department.setId(1);
        HashMap hashMap = new HashMap();
        hashMap.put("number",1);
        hashMap.put("prefix","k");

        Mockito.when(departmentRepositorys.existsByDeptname(user.getDepartment())).thenReturn(true);
        Mockito.when(departmentRepositorys.findById(department.getId())).thenReturn(Optional.of(department));
        Mockito.when(numberUser.getNumberAndPrefix(Optional.of(department))).thenReturn(hashMap);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        var data = userService.add(user);
        JSONObject jsonObject = new JSONObject(data);
        Assertions.assertEquals(user.getId(),jsonObject.get("id"));

    }

}
