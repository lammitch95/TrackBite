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

    public User(String userId, String username, String password, String firstName, String lastName,String dob,String phoneNum,String role){
        this.id = userId;
        this.username = username;
        this.password = password;
        this.fName = firstName;
        this.lName = lastName;
        this.dob = dob;
        this.phoneNum = phoneNum;
        this.role = role;
    }

    public String getId(){return this.id;}
    public String getUsername(){return this.username;}
    public String getFirstName(){return this.fName;}

    public String getRole(){return this.role;}
}
