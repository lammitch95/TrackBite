package chooser.model;

public class Employee {
    private final String firstName;
    private final String lastName;
    private final String DOB;
    private final String phoneNum;
    private final String empID;
    private final String password;

    public Employee(String firstName, String lastName, String DOB, String phoneNum) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.DOB = DOB;
        this.phoneNum = phoneNum;
        this.empID = createEmpID();
        this.password = createEmpPW();
    }

    // create employee id
    private String createEmpID() {
        String empID = lastName + firstName.charAt(0);

        // if there is duplicate empId's ie; John Smith and Jason Smith would both be SmithJ

        int empCount = 1;

        while (registerUser.isDuplicateID(empID + empCount)) {
            empCount++;
        }
        return empID + empCount;
    }

    //Make PW the last 4 digits of the phone number for now
    private String createEmpPW() {
        return phoneNum.length() >= 4 ? phoneNum.substring(phoneNum.length() - 4) : "0000";
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmpID() {
        return empID;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Employee ID='" + empID + " , First Name= " + firstName + " , Last Name=" + lastName + "', Date of Birth='" + DOB + "', Phone Number='" + phoneNum + "'}";
    }

}
