package app.unit.service.userservice;

import app.component.NumberUser;
import app.exception.СustomException;
import app.model.User;
import app.repository.DepartmentRepository;
import app.repository.UserRepository;
import app.service.UserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class Search {
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
    public void sendAllArgumentsAreNull(){
        User user = new User(null,null,null,null,null,null);
        var excp = Assertions.assertThrows(СustomException.class,()->{userService.search(user);});
        Assertions.assertEquals("Тело запроса должно содержать поле name,patronomic,surname или number или department",excp.getMessage());
        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());
    }

    @Test
    public void sendThreeArguments() throws СustomException, JSONException {
        User userRequest = new User(null,"test","test1","test2",null,null);
        User userResponse = new User(1,"test","test1","test2","1","отдел 1");
        List<User> userList = new ArrayList<>();
        userList.add(userResponse);

        Mockito.when(userRepository.findByNameAndPatronomicAndSurnameOrNumberOrDepartmentFk(userRequest.getName(),userRequest.getPatronomic(),
                userRequest.getSurname(),userRequest.getNumber(),userRequest.getDepartment())).thenReturn(userList);

        var data = userService.search(userRequest);
        JSONArray jsonArray = new JSONArray(data);

        Assertions.assertEquals(userResponse.getId(),jsonArray.getJSONObject(0).get("id"));
        Assertions.assertEquals(userResponse.getName(),jsonArray.getJSONObject(0).get("name"));
        Assertions.assertEquals(userResponse.getPatronomic(),jsonArray.getJSONObject(0).get("patronomic"));
        Assertions.assertEquals(userResponse.getSurname(),jsonArray.getJSONObject(0).get("surname"));
        Assertions.assertEquals(userResponse.getNumber(),jsonArray.getJSONObject(0).get("number"));
        Assertions.assertEquals(userResponse.getDepartment(),jsonArray.getJSONObject(0).get("department"));
    }

    @Test
    public void notFoundUser() throws СustomException {
        User user = new User(null,"test",null,null,null,null);
        List<User> userList = new ArrayList<>();
        Mockito.when(userRepository.findByNameAndPatronomicAndSurnameOrNumberOrDepartmentFk(user.getName(),user.getPatronomic(),
                user.getSurname(),user.getNumber(),user.getDepartment())).thenReturn(userList);

        var excp = Assertions.assertThrows(СustomException.class,()->{userService.search(user);});
        Assertions.assertEquals("Поиск ничего не нашел",excp.getMessage());
        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());
    }
}
