package app.model;

import jakarta.persistence.*;
import lombok.ToString;

import java.io.Serializable;


@Entity
@Table(name = "users",schema = "public")
@ToString
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "first_name")
    private String name;
    private String patronomic;
    @Column(name = "last_name")
    private String surname;
    @Column(name = "serial_number",unique = true)
    private String number;
    @Column(name = "department_fk")
    private String departmentFk;

    public User(){

    }
    public User(Integer id,String name,String patronomic,String surname,String number, String departmentFk){
        this.id = id;
        this.name = name;
        this.patronomic = patronomic;
        this.surname = surname;
        this.number = number;
        this.departmentFk = departmentFk;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setPatronomic(String patronomic){
        this.patronomic = patronomic;
    }

    public String getPatronomic(){
        return patronomic;
    }
    public void setSurname(String surname){
        this.surname = surname;
    }

    public String getSurname(){
        return surname;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public String getNumber(){
        return number;
    }

    public void setDepartment(String departmentFk){
        this.departmentFk = departmentFk;
    }

    public String getDepartment(){
        return departmentFk;
    }

}
