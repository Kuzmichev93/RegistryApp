package app.unit.service.userservice;

import app.component.NumberUser;
import app.exception.СustomException;
import app.model.User;
import app.repository.DepartmentRepository;
import app.repository.UserRepository;
import app.service.UserService;
import net.minidev.json.JSONObject;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class Delete {
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
    public void userGetNumberIsNull(){
        User user = new User(1,"test1","test2","test3",null,"test 4");
        var excp = Assertions.assertThrows(СustomException.class,()->{userService.delete(user);});
        Assertions.assertEquals("Тело запроса содержит невалидное значение",excp.getMessage());
        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());
    }

    @Test
    public void userGetNumberIsNotNullandUserExistsInBd() throws JSONException, СustomException {
        User user = new User(1,"test1","test2","test3","1k","test 4");
        Mockito.when(userRepository.existsByNumber(user.getNumber())).thenReturn(true);
        Mockito.doNothing().when(userRepository).deleteByNumber(user.getNumber());
        var data = userService.delete(user);
        JSONObject jsonObject = new JSONObject();
        Assertions.assertNotNull(jsonObject.get("message"));
        Assertions.assertEquals("Пользователь удален",jsonObject.get("message"));
    }

    @Test
    public void userGetNumberIsNotNullandUserNotExistsInBd(){
        User user = new User(1,"test1","test2","test3","1k","test 4");
        Mockito.when(userRepository.existsByNumber(user.getNumber())).thenReturn(false);
        var excp = Assertions.assertThrows(СustomException.class,()->{userService.delete(user);});
        Assertions.assertEquals("Пользователь не существует в бд",excp.getMessage());
        Assertions.assertEquals("400 BAD_REQUEST",excp.getStatus().toString());
    }


}
