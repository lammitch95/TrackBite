package chooser.model;

public class User {
    String id;
    String username;
    String password;
    String fName;
    String lName;
    String dob;
    String phoneNum;
    String role;

    public User(String userId, String username, String firstName, String lastName,String dob,String phoneNum,String role,String password){
        this.id = userId;
        this.username = username;
        this.fName = firstName;
        this.lName = lastName;
        this.dob = dob;
        this.phoneNum = phoneNum;
        this.role = role;
        this.password = password;

    }

    public String getId(){return this.id;}
    public String getUsername(){return this.username;}
    public String getFirstName(){return this.fName;}
    public String getLastName(){return  this.lName;}
    public String getDob(){return  this.dob;}
    public String getPhoneNum(){return this.phoneNum;}
    public String getPassword(){return this.password;}

    public String getRole(){return this.role;}
}
