package com.minewbeacon.blescan.demo;

public class UserAccount
{
    private String idToken;     // Firebase 고유번호
    private String emailId;
    private String password;
    private String name;
    private String work_start;

    public UserAccount() {}

    public  String getIdToken() { return idToken;}
    public void setIdToken(String idToken) { this.idToken = idToken;}

    public  String getEmailId() { return emailId;}
    public void setEmailId(String emailId) { this.emailId = emailId;}

    public  String getPassword() { return password;}
    public void setPassword(String password) { this.password = password;}

    public  String getName() { return name;}
    public void setName(String name) { this.name = name;}

    public  String getWork_start() { return work_start;}
    public void setWork_start(String work_start) { this.work_start = work_start;}

}