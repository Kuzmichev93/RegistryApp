package app.service;

import app.exception.СustomException;
import app.model.Department;
import app.repository.DepartmentRepository;
import app.utils.Reg;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Cacheable(value = "set5min",key = "'deptAll'")
    public String get() throws СustomException {
        try{
            List<Department> departmentList = departmentRepository.findAll();
            if(departmentList.size() == 0){
                throw new СustomException("Департаменты в бд не найдены", HttpStatus.BAD_REQUEST);
            }
            JSONArray jsonArray = new JSONArray();
            for(int i = 0;i<departmentList.size();i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",departmentList.get(i).getId());
                jsonObject.put("deptname",departmentList.get(i).getDeptname());
                jsonObject.put("prefix",departmentList.get(i).getPrefix());
                jsonArray.put(jsonObject);
            }
            return jsonArray.toString();
        }
        catch (Exception e){
            throw new СustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }

    @Caching(evict = {@CacheEvict(value = "set5min",key = "'deptAll'")},
            cacheable = {@Cacheable(value = "set5min",key = "#department.deptname")},
            put = {@CachePut(value = "set5min",key = "#department.deptname")}
           )
    public String add(Department department) throws СustomException {
        Reg reg = new Reg();
        if(department.getDeptname() == null || department.getPrefix() == null){
            throw new СustomException("Поле deptname и prefix должны быть в запросе", HttpStatus.BAD_REQUEST);
        }
        if(reg.checkValue("№?[0-9]+[a-zA-Zа-я-А-Я]+",department.getDeptname())
                || reg.checkValue("[a-zA-Zа-я-А-Я]+",department.getPrefix())){
            throw new СustomException("Объект содержит невалидные данные", HttpStatus.BAD_REQUEST);
        }
        try {
            JSONObject jsonObject = new JSONObject();
            Department data = departmentRepository.save(department);
            jsonObject.put("id",data.getId());
            jsonObject.put("deptname",data.getDeptname());
            jsonObject.put("prefix",data.getPrefix());
            return jsonObject.toString();

        }
        catch (Exception e){
            throw new СustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CacheEvict(value = "set5min",key = "'deptAll'")
    public String addAll(List<Department> department) throws СustomException, JSONException {
        Reg reg = new Reg();
        if(department.size() == 0){
            throw new СustomException("Должен быть передан список содержащий deptname и prefix", HttpStatus.BAD_REQUEST);
        }
        for (int i = 0;i<department.size();i++){
            if(department.get(i).getDeptname() == null || department.get(i).getPrefix() == null){
                throw new СustomException(String.format("Объект с индексом %d должен содержать поля deptname и prefix",i+1), HttpStatus.BAD_REQUEST);
            }
            if(reg.checkValue("№?[0-9]+[a-zA-Zа-я-А-Я]+",department.get(i).getDeptname())
                    || reg.checkValue("[a-zA-Zа-я-А-Я]+",department.get(i).getPrefix())){
                throw new СustomException(String.format("Объект с индексом %d содержит невалидные значения",i+1), HttpStatus.BAD_REQUEST);
            }

        }
        try{
            departmentRepository.saveAll(department);
        }
        catch (Exception e){
            throw new СustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Message","Департаменты добавлены в бд");

        return jsonObject.toString();

    }

    @Cacheable(value = "set5min",key = "#department.deptname")
    public String search(Department department) throws СustomException {
        Reg reg = new Reg();
        if(department.getDeptname() == null){
            throw new СustomException("Поле deptname должно быть в запросе", HttpStatus.BAD_REQUEST);
        }
        if(!departmentRepository.existsByDeptname(department.getDeptname())){
            throw new СustomException("Департамент не существует в бд", HttpStatus.BAD_REQUEST);
        }

        if(reg.checkValue("№?[0-9]+[a-zA-Zа-я-А-Я]+",department.getDeptname())){
            throw new СustomException("Объект содержит невалидные данные", HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<Department> data = departmentRepository.findByDeptname(department.getDeptname());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",data.get().getId());
            jsonObject.put("deptname",data.get().getDeptname());
            jsonObject.put("prefix",data.get().getPrefix());
            return jsonObject.toString();

        }
        catch (Exception e){
            throw new СustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @CacheEvict(value = "set5min",allEntries = true)
    public String delete(Department department) throws СustomException {
        Reg reg = new Reg();
        if(department.getDeptname() == null){
            throw new СustomException("Поле deptname должно быть в запросе", HttpStatus.BAD_REQUEST);
        }
        if(reg.checkValue("№?[0-9]+[a-zA-Zа-я-А-Я]+",department.getDeptname())){
            throw new СustomException("Объект содержит невалидные данные", HttpStatus.BAD_REQUEST);
        }
        JSONObject jsonObject = new JSONObject();
        if(departmentRepository.existsByDeptname(department.getDeptname())){
            try{
                departmentRepository.deleteByDeptname(department.getDeptname());
                jsonObject.put("Message","Департамент удален");
                return jsonObject.toString();

            }
            catch (Exception e){
                    throw new СustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        throw new СustomException("Департамент не существует в бд", HttpStatus.BAD_REQUEST);



    }

}
