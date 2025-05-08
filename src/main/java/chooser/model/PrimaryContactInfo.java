package chooser.model;

public class PrimaryContactInfo {

    private String firstName;
    private String lastName;
    private String phoneNum;
    private String email;
    private String website;

    public PrimaryContactInfo(String firstName, String lastName, String phoneNum, String email, String website){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
        this.email = email;
        this.website = website;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
    @Override
    public String toString() {
        return firstName + " " + lastName + "/" + phoneNum + "/" + email + "/" + website;
    }
}
