package app.service;

import app.component.NumberUser;
import app.exception.СustomException;
import app.model.Department;
import app.model.User;
import app.repository.DepartmentRepository;
import app.repository.UserRepository;
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

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {



    @Autowired
    private DepartmentRepository departmentRepository;
    private UserRepository userRepository;
    private NumberUser numberUser;
    public UserService(NumberUser numberUser,DepartmentRepository departmentRepository,UserRepository userRepository){
        this.numberUser = numberUser;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;

    }
    @Cacheable(value = "set5min",key = "'userAll'")
    public String get() throws JSONException, СustomException {
        try{
            List<User> data = userRepository.findAll();
            if(data.size() == 0){
                throw new СustomException("Сотрудники в бд не найдены", HttpStatus.BAD_REQUEST);
            }
            JSONArray jsonArray = new JSONArray();
            for(int i = 0;i<data.size();i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",data.get(i).getId());
                jsonObject.put("name",data.get(i).getName());
                jsonObject.put("surname",data.get(i).getSurname());
                jsonObject.put("patronomic",data.get(i).getPatronomic());
                jsonObject.put("number",data.get(i).getNumber());
                jsonObject.put("department",data.get(i).getDepartment());
                jsonArray.put(jsonObject);

            }
            return jsonArray.toString();
        }
        catch (Exception e){
            throw new СustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @Caching(evict = {@CacheEvict(value = "set5min",key = "'userAll'")},
            cacheable ={@Cacheable(value = "set5min",key = "{#user.name,#user.patronomic,#user.surname,#user.department}")},
            put ={@CachePut(value = "set5min",key = "{#user.name,#user.patronomic,#user.surname,#user.department}")} )
    public String add(User user) throws СustomException, JSONException {
            Reg reg = new Reg();
            JSONObject jsonObject = new JSONObject();
            if(user.getName() == null || user.getPatronomic() == null || user.getSurname() == null || user.getDepartment() == null ){
                if(userRepository.existsByNumber(user.getNumber())){
                    throw new СustomException("Пользователь уже существует в бд", HttpStatus.BAD_REQUEST);
                }
                /*Если в теле запроса будут введены другие атрибуты, то сработает исключение */
                throw new СustomException("Поле name,patronomic,surname и department не должны быть пустыми", HttpStatus.BAD_REQUEST);

            }
            if(reg.checkValue("[A-ZА-Я]{1}[a-zа-я]*",user.getName()) ||
                    reg.checkValue("(?:[A-ZА-Я]{1}[a-zа-я]*-[A-ZА-Я]{1}[a-zа-я]+|[A-ZА-Я]{1}[a-zа-я]*)",user.getSurname())||
                reg.checkValue("[A-ZА-Я]{1}[a-zа-я]*",user.getPatronomic()) ||
                reg.checkValue("№[0-9]+[a-zA-Zа-яА-Я]+",user.getDepartment())
              ){

                throw new СustomException("Поле name или patronomic или surname или department содержит невалидные данные", HttpStatus.BAD_REQUEST);
            }

            if (departmentRepository.existsByDeptname(user.getDepartment())) {
                Optional<Department> data = departmentRepository.findByDeptname(user.getDepartment());
                /*Метод getNumberAndPrefix на вход получает ссылку на объект Department.
                * В теле метода по первичному ключу Department в основной хеш-таблице ищется ссылка на другую хеш-таблицу,
                * которая содержит ключи number и prefix. Если по первичному ключу Department не найдена ссылка,
                * то будет создана новая хеш-таблица с ключами number, prefix и данная ссылка будет добавлена в основную хеш-таблицу по первичному ключу Department.
                * На выходе будет ссылка на основную хеш-таблицу.*/

                HashMap<String, HashMap> numberAndPrefix = numberUser.getNumberAndPrefix(data);
                Integer number = (Integer) numberAndPrefix.get(data.get().getDeptname()).get("number");//Порядковый номер пользователя
                String prefix = (String) numberAndPrefix.get(data.get().getDeptname()).get("prefix");//Префикс порядкого номера
                user.setNumber(number.toString() + prefix);//Объединяем порядковый номер и префикс
                try{
                    userRepository.save(user);//Сохраняем сущность user в бд
                }
                catch (Exception e){
                    throw new СustomException(e.getMessage(),HttpStatus.BAD_REQUEST);
                }

                number += 1;
                numberAndPrefix.get(data.get().getDeptname()).put("number", number);//Записываем в hashmap новое значение number

                jsonObject.put("id", user.getId());
                jsonObject.put("name", user.getName());
                jsonObject.put("patronomic", user.getPatronomic());
                jsonObject.put("surname", user.getSurname());
                jsonObject.put("number", user.getNumber());
                jsonObject.put("department", user.getDepartment());
                return jsonObject.toString();


            } else {
                /*Метод deleteNumberAndPrefix на вход получает первичный ключ Department.
                * В теле метода по первичному ключу Department происходит поиск в хеш-таблице.
                * Если значение будет найдено, то произойдет удаление из хеш-таблицы */
                numberUser.deleteNumberAndPrefix(user.getDepartment());
                throw new СustomException("Атрибут department содержит несуществующий id",HttpStatus.BAD_REQUEST);
            }

    }
    @CacheEvict(value = "set5min",key = "'userAll'")
    public String addAll(List<User> user) throws СustomException, JSONException {
        Reg reg = new Reg();

        if(user.size() == 0){
            throw new СustomException("Должен быть передан список содержащий deptname и prefix", HttpStatus.BAD_REQUEST);
        }
        //Проверка входных данных списка
        for (int i = 0;i<user.size();i++){
            if(user.get(i).getName() == null || user.get(i).getPatronomic() == null || user.get(i).getSurname() == null || user.get(i).getDepartment() == null){
                throw new СustomException(String.format("Объект с индексом %d должен содержать поля deptname и prefix",i+1), HttpStatus.BAD_REQUEST);
            }
            if(reg.checkValue("[A-ZА-Я]{1}[a-zа-я]*",user.get(i).getName()) ||
                    reg.checkValue("(?:[A-ZА-Я]{1}[a-zа-я]*-[A-ZА-Я]{1}[a-zа-я]+|[A-ZА-Я]{1}[a-zа-я]*)",user.get(i).getSurname())||
                    reg.checkValue("[A-ZА-Я]{1}[a-zа-я]*",user.get(i).getPatronomic()) ||
                    reg.checkValue("№[0-9]+[a-zA-Zа-яА-Я]+",user.get(i).getDepartment())
            ){
                throw new СustomException(String.format("Объект с индексом %d содержит невалидные данные",i+1), HttpStatus.BAD_REQUEST);
            }
            if (!departmentRepository.existsByDeptname(user.get(i).getDepartment())) {
                throw new СustomException(String.format("В поле departament введен не существующий ид. Индекс в списке %d",i+1), HttpStatus.BAD_REQUEST);
            }

        }
        //Создание порядкового номера
        for(int k = 0;k<user.size();k++){
            Optional<Department> data = departmentRepository.findByDeptname(user.get(k).getDepartment());
            HashMap<String, HashMap> numberAndPrefix = numberUser.getNumberAndPrefix(data);
            Integer number = (Integer) numberAndPrefix.get(data.get().getDeptname()).get("number");//Порядковый номер пользователя
            String prefix = (String) numberAndPrefix.get(data.get().getDeptname()).get("prefix");//Префикс порядкого номера
            user.get(k).setNumber(number.toString() + prefix);

            number += 1;
            numberAndPrefix.get(data.get().getDeptname()).put("number", number);
        }
        //Запись в бд
        try {
            userRepository.saveAll(user);
        }
        catch (Exception e){
            throw new СustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Message","Сотрудники добавлены в бд");


        return jsonObject.toString();

    }

    @Cacheable(value = "set5min",key = "{#user.name,#user.patronomic,#user.surname,#user.department}")
    public String search(User user) throws СustomException, JSONException {

        JSONArray jsonArray = new JSONArray();
        List<User> data;
        if(user.getName()!=null && user.getPatronomic()!=null && user.getSurname()!=null){
            data = userRepository.findByNameAndPatronomicAndSurnameOrNumberOrDepartmentFk(user.getName(),user.getPatronomic(),
                    user.getSurname(),null,null);
        }
        else{
            if(user.getNumber()!=null){
                data = userRepository.findByNameAndPatronomicAndSurnameOrNumberOrDepartmentFk(null,null,
                        null,user.getNumber(),null);
            }
            else{
                if(user.getDepartment()!=null){
                    data = userRepository.findByNameAndPatronomicAndSurnameOrNumberOrDepartmentFk(null,null,
                            null,null,user.getDepartment());
                }
                else {
                    throw new СustomException("Тело запроса должно содержать поле name,patronomic,surname или number или department", HttpStatus.BAD_REQUEST);
                }
            }
        }
        if(data.size() != 0){
            for(int i = 0;i<data.size();i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", data.get(i).getId());
                jsonObject.put("name", data.get(i).getName());
                jsonObject.put("patronomic",data.get(i).getPatronomic());
                jsonObject.put("surname",data.get(i).getSurname());
                jsonObject.put("number",data.get(i).getNumber());
                jsonObject.put("department",data.get(i).getDepartment());
                jsonArray.put(jsonObject);
            }
            return jsonArray.toString();
        }
        else{
            throw new СustomException("Поиск ничего не нашел",HttpStatus.BAD_REQUEST);
        }

    }

    @Caching(evict = {@CacheEvict(value = "set5min",key = "'userAll'")},
             put = {@CachePut(value = "set5min",key = "{#user.name,#user.patronomic,#user.surname,#user.department}")})
    public String put(User user) throws СustomException {
        Reg reg = new Reg();
        if(user.getName() == null || user.getPatronomic() == null || user.getSurname() == null || user.getNumber() == null ){
            throw new СustomException("Поле number,patronomic,surname и number не должны быть пустыми", HttpStatus.BAD_REQUEST);
        }
        if(reg.checkValue("[A-ZА-Я]{1}[a-zа-я]*",user.getName()) ||
                reg.checkValue("(?:[A-ZА-Я]{1}[a-zа-я]*-[A-ZА-Я]{1}[a-zа-я]+|[A-ZА-Я]{1}[a-zа-я]*)",user.getSurname())||
                reg.checkValue("[A-ZА-Я]{1}[a-zа-я]*",user.getPatronomic()) ||
                reg.checkValue("№[0-9]+[a-zA-Zа-яА-Я]+",user.getDepartment())
        ){
            throw new СustomException("Поле name или patronomic или surname или department содержат невалидные данные", HttpStatus.BAD_REQUEST);
        }
        try{
            if(userRepository.existsByNumber(user.getNumber())){
                JSONObject jsonObject = new JSONObject();
                User data = userRepository.findByNumber(user.getNumber());
                data.setName(user.getName());
                data.setPatronomic(user.getPatronomic());
                data.setSurname(user.getSurname());
                User userupdate = userRepository.save(data);

                jsonObject.put("name", userupdate.getName());
                jsonObject.put("patronomic",userupdate.getPatronomic());
                jsonObject.put("surname",userupdate.getSurname());
                jsonObject.put("number",userupdate.getNumber());
                jsonObject.put("department",userupdate.getDepartment());
                return jsonObject.toString();


            }
            else{
                throw new СustomException("Пользователь не существует в бд",HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e){
            throw new СustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CacheEvict(value = "set5min",allEntries = true)
    public String delete(User user) throws СustomException, JSONException {
        if(user.getNumber() != null){
            try{
                if(userRepository.existsByNumber(user.getNumber())){
                    JSONObject jsonObject = new JSONObject();
                    userRepository.deleteByNumber(user.getNumber());
                    jsonObject.put("message","Пользователь удален");
                    return jsonObject.toString();
                }
                else {
                    throw new СustomException("Пользователь не существует в бд",HttpStatus.BAD_REQUEST);
                }
            }
            catch (Exception e){
                throw new СustomException(e.getMessage(),HttpStatus.BAD_REQUEST);
            }

        }
        else{
            throw new СustomException("Тело запроса содержит невалидное значение",HttpStatus.BAD_REQUEST);
        }
    }



}
