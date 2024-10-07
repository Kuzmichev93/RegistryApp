package app.controller;

import app.exception.СustomException;
import app.model.User;
import app.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/user")
public class UserController {



    @Autowired
    private UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    public UserController(){

    }

    @GetMapping(value = "all",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getDepartment() throws СustomException, JSONException {
        String jsonArray = userService.get();
        return new ResponseEntity<>(jsonArray, HttpStatus.OK);
    }

    @PostMapping(value = "createall",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUsers(@RequestBody List<User> user) throws JSONException, СustomException {
        String data = userService.addAll(user);

        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    @PostMapping(value = "search",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchUser(@RequestBody User user) throws СustomException, JSONException, JsonProcessingException {
        String data = userService.search(user);

        return new ResponseEntity<>(data,HttpStatus.OK);


    }


    @PostMapping(value = "create",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@RequestBody User user) throws СustomException, JSONException {
        String data = userService.add(user);

        return new ResponseEntity<>(data, HttpStatus.OK);

    }
    @PutMapping(value = "update",produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<String> updateUser(@RequestBody User user) throws JSONException, СustomException {
        String data = userService.put(user);
        return new ResponseEntity<>(data, HttpStatus.OK);

    }
    @DeleteMapping(value = "delete",produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<String> deleteUser(@RequestBody User user) throws JSONException, СustomException {
        String data = userService.delete(user);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
