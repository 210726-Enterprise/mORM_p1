package Model;

import Annotations.Column;
import Annotations.Entity;
import Annotations.Primary;

@Entity(tableName="users")
public class User {
    @Primary(keyName="userID")
    private int userID;
    @Column(columnName="first_name")
    private String first_name;
    @Column(columnName="last_name")
    private String last_name;
    @Column(columnName="email")
    private String email;
    @Column(columnName="password")
    private String password;

    //Constructor
    public User(String f, String l, String e, String p){
        first_name = f;
        last_name = l;
        email = e;
        password = p;
    }

    //Default Constructor
    public User() {}

    //Getters
    public int getUserID(){
        return userID;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    //Setters
    public void setUserID(int u) {
        this.userID = u;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}