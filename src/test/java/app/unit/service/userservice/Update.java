package app.unit.service.userservice;

import app.component.NumberUser;
import app.exception.СustomException;
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

public class Update {
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
    public void sendAllArgumentsAreNullPut(){
        User user = new User(null,null,null,null,null,null);
        var excp = Assertions.assertThrows(СustomException.class,()->{userService.put(user);});
        Assertions.assertEquals("Поле number,patronomic,surname и number не должны быть пустыми",excp.getMessage());
        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());
    }

    @Test
    public void userExistsInBd() throws СustomException, JSONException {
        User user = new User(1,"test1","test2","test3","1k","test 4");
        Mockito.when(userRepository.existsByNumber(user.getNumber())).thenReturn(true);

        User userResponse = new User(1,"test1","test2","test3","1k","test 4");
        Mockito.when(userRepository.findByNumber(user.getNumber())).thenReturn(userResponse);
        User userUpdate = new User(1,"test1","new","test3","1k","test 4");
        Mockito.when(userRepository.save(userResponse)).thenReturn(userUpdate);
        var data = userService.put(user);
        JSONObject jsonObject = new JSONObject(data);
        Assertions.assertEquals("test1",jsonObject.get("name"));
        Assertions.assertEquals("new",jsonObject.get("patronomic"));
        Assertions.assertEquals("test3",jsonObject.get("surname"));
        Assertions.assertEquals("1k",jsonObject.get("number"));
        Assertions.assertEquals("test 4",jsonObject.get("department"));


    }
    @Test
    public void userNotExistInBd(){
        User user = new User(1,"test1","test2","test3","1k","test 4");
        Mockito.when(userRepository.existsByNumber(user.getNumber())).thenReturn(false);
        var excp = Assertions.assertThrows(СustomException.class,()->{userService.put(user);});
        Assertions.assertEquals("Пользователь не существует в бд",excp.getMessage());
        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());
    }

}
