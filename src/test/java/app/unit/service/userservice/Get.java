package app.unit.service.userservice;

import app.exception.СustomException;
import app.model.User;
import app.repository.UserRepository;
import app.service.UserService;
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
    private UserService userService;


    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void usersExistsInBd() throws СustomException, JSONException {
        List<User> users = new ArrayList<>();
        users.add(new User(1,"test","test2","test3","234","test4"));
        users.add(new User(2,"test1","test21","test31","2341","test41"));
        Mockito.when(userRepository.findAll()).thenReturn(users);

        var data = userService.get();
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        Assertions.assertNotNull(data);
        Assertions.assertEquals(2,data.length());
        Assertions.assertEquals("test",jsonArray.getJSONObject(0).get("name"));
        Assertions.assertEquals("test1",jsonArray.getJSONObject(1).get("name"));
    }

    @Test
    public void departmentsNotExist() throws СustomException {
        List<User> users = new ArrayList<>();
        Mockito.when(userRepository.findAll()).thenReturn(users);

        var excp = Assertions.assertThrows(СustomException.class,()->userService.get());
        Assertions.assertEquals("Сотрудники в бд не найдены",excp.getMessage());
    }
}
