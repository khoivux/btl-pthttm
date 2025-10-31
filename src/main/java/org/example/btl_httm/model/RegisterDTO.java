package org.example.btl_httm.model;

public class RegisterDTO {
    private String username;
    private String fullname;
    private String email;
    private String phonenumber;
    private String password;
    private String confirmPassword;

    public RegisterDTO() {
    }

    public RegisterDTO(String username, String fullname, String email, String phonenumber, String password, String confirmPassword) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
