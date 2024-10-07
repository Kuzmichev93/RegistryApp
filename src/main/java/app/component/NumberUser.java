package app.component;

import app.model.Department;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

@Component
public class NumberUser {
    public HashMap<String,HashMap> parent = new HashMap<>();

    public HashMap<String,HashMap> getNumberAndPrefix(Optional<Department> departmentOptional){
        if(parent.containsKey(departmentOptional.get().getDeptname())){
            parent.get(departmentOptional.get().getId());
            return parent;

        }
        else{
            HashMap hashMap = new HashMap();
            hashMap.put("number",1);
            hashMap.put("prefix",departmentOptional.get().getPrefix());
            parent.put(departmentOptional.get().getDeptname(),hashMap);
            return parent;
        }
    }

    public void deleteNumberAndPrefix(String key){
        if(parent.containsKey(key)){
            parent.remove(key);
        }

    }
}
