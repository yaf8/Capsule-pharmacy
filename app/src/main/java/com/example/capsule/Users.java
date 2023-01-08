package com.example.capsule;

public class Users {

    private String FirstName;
    private String LastName;
    private String PhoneNumber;
    private String Url;
    private String Email;
    private boolean isAdmin;
    private boolean isDeleted;

    public Users(String email){
        this.Email = email;
    }

    public Users(String firstName, String lastName, String phoneNumber, String url, String email, boolean isAdmin, boolean isDeleted) {
        FirstName = firstName;
        LastName = lastName;
        PhoneNumber = phoneNumber;
        Url = url;
        Email = email;
        this.isAdmin = isAdmin;
        this.isDeleted = isDeleted;
    }

    public Users() {
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUrl() {
        return Url;
    }

    public String getFullName(){
        return getFirstName() + " " + getLastName();
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
