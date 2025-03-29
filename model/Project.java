
import java.util.*;

public class Project {
    private int id;
    private String name;
    private String description;
    private List<String> bugs;
    
    public Project(int id,String name,String description,List<String> bugs){
        this.id=id;
        this.name=name;
        this.description=description;
        this.bugs=bugs;
    }

    public int getter_id(){
        return id;
    }

    public String getter_name(){
        return name;
    }

    public String getter_description(){
        return description;
    }

    public List<String> getter_bugs(){
        return bugs;
    }

    public void setter_id(int id){
        this.id=id;
    }

    public void setter_name(String name){
        this.name=name;
    }

    public void setter_description(String description){
        this.description=description;
    }

    public void setter_bugs(List<String>bugs){
        this.bugs=bugs;
    }
}