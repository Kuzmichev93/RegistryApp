package app.controller;


import app.exception.СustomException;
import app.model.Department;
import app.service.DepartmentService;
import jakarta.transaction.Transactional;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping(value = "all",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getDepartment() throws СustomException, JSONException {
        String jsonArray = departmentService.get();
        return new ResponseEntity<>(jsonArray, HttpStatus.OK);
    }

    @PostMapping(value = "create",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveDepartment(@RequestBody Department department) throws СustomException {
        String jsonObject = departmentService.add(department);
        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }

    @PostMapping(value = "createall",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> savesDepartment(@RequestBody List<Department> department) throws СustomException, JSONException {
        String jsonObject = departmentService.addAll(department);
        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }

    @PostMapping(value = "search",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchDepartment(@RequestBody Department department) throws СustomException {
        String jsonObject = departmentService.search(department);
        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }

    @DeleteMapping(value = "delete",produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<String> deleteDepartment(@RequestBody Department department) throws СustomException {
        String jsonObject = departmentService.delete(department);
        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }



}
