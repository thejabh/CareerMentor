package com.example.careermentor;

public class User {

    public String username,password,role,desc;
    public User(){}
    public User(String uname,String pass,String role,String desc){
        this.username=uname;
        this.password=pass;
        this.role=role;
        this.desc = desc;
    }
    public void setData(String uname,String pass,String role,String desc){
        this.username=uname;
        this.password=pass;
        this.role=role;
        this.desc = desc;
    }
    public String getUsername(){return username;}
    public String getDesc(){return desc;}
    public String getPassword() {
        return password;
    }
    public String getRole(){
        return role;
    }
}