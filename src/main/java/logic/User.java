package logic;

import database.DataBase;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "login", "nameUser", "branches"})
public class User {
    @XmlElement(name = "userId")
    private int id;
    @XmlElement(name = "userLogin")
    private String login;
    @XmlTransient
    private String password;
    @XmlElement(name = "userName")
    private String nameUser;

    @XmlElementWrapper(name = "BranchList")
    @XmlElement(name = "Branch")
    private List<Branch> branches;

    public User(){
        this.id = DataBase.getId();
        branches = new ArrayList<Branch>();
    }

    public User(String login, String nameUser, String password){
        this.id = DataBase.getId();
        this.login = login;
        this.nameUser = nameUser;
        this.password = password;
        this.branches = new ArrayList<Branch>();
    }

    public User(int id, String login, String nameUser, String password, List<Branch> branches){
        this.id = id;
        this.login = login;
        this.nameUser = nameUser;
        this.password = password;
        this.branches = branches;
    }

    public User(String login, String nameUser, String password, List<Branch> branches){
        this.id = DataBase.getId();
        this.login = login;
        this.nameUser = nameUser;
        this.password = password;
        this.branches = branches;
    }

    public User(int id, String login, String nameUser) {
        this.id = id;
        this.login = login;
        this.nameUser = nameUser;
        this.branches = new ArrayList<Branch>();
    }

    public User(int id, String login) {
        this.id = id;
        this.login = login;
        this.nameUser = "";
        this.branches = new ArrayList<Branch>();
    }

    public User(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.nameUser = user.getNameUser();
        this.branches = user.getBranch();
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getNameUser() {
        return nameUser;
    }

    public String getPassword() {
        return password;
    }

    public List<Branch> getBranch() {
        return branches;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBranch(List<Branch> branches) {
        this.branches = branches;
    }

    public int getLength(){ return this.branches.size(); }

    public void addElementToIndex(int index, Branch branch){
        this.branches.add(index, branch);
    }

    public void addElement(Branch branch){
        this.branches.add(branch);
    }

    public void deleteElement(int index){
        this.branches.remove(index);
    }

    public void listSort(){
        try {
            Collections.sort(this.branches);
        }catch (Error e){
            System.out.println(e);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", nameUser='" + nameUser + '\'' +
                ", arrayCouch=" + branches.toString() +
                '}';
    }
}