package chooser.model;
import java.util.ArrayList;
import java.util.List;
public class registerUser {

    private static final List<Employee> employees = new ArrayList<>();

    //check if the generated emp id is taken/ does it need a count

    public static boolean isDuplicateID(String empID) {
        for (Employee emp : employees) {
            if (emp.getEmpID().equals(empID)) {
                return true;
            }
        }
        return false;
    }
    // create employee
    public static void registerEmployee(String firstName, String lastName, String DOB, String phoneNum) {
        Employee newEmployee = new Employee(firstName, lastName, DOB, phoneNum);
        employees.add(newEmployee);
    }
    //show employees
    public static List<Employee> getEmployees() {
        return employees;
    }
}
