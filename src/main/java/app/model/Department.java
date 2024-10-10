package app.model;

import jakarta.persistence.*;
import lombok.ToString;

import java.io.Serializable;


@Entity
@Table(name = "departments",schema = "public")
@ToString
public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String deptname;
    @Column(unique = true)
    private String prefix;

    public Department(){

    }
    public Department(Integer id,String deptname,String prefix){
        this.id = id;
        this.deptname = deptname;
        this.prefix = prefix;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public Integer getId(){
        return id;
    }

    public void setDeptname(String deptname){
        this.deptname = deptname;
    }

    public String getDeptname(){
        return deptname;
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    }
    public String getPrefix(){
        return prefix;
    }
}
