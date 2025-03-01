package chooser.utils;

import java.util.regex.Pattern;

public class NewUserUtils {

    public static boolean isValidName(String name) {
        if (name == null) return false;
        return name.trim().length() >= 3;
    }

    public static boolean isValidDateOfBirth(String dob) {
        //format Example is MM/DD/YYYY
        String regex = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\\d{4}$";
        return dob != null && Pattern.matches(regex, dob);
    }

    public static boolean isValidPhoneNum(String phoneNum) {
        //format Example 000-000-0000
        String regex = "^\\d{3}-\\d{3}-\\d{4}$";
        return phoneNum != null && Pattern.matches(regex, phoneNum);
    }


    public static String formatPhoneNumber(String input) {
        if (input == null) return "";

        String digits = input.replaceAll("\\D", "");

        if (digits.length() > 10) {
            digits = digits.substring(0, 10);
        }
        StringBuilder formatted = new StringBuilder();
        int len = digits.length();
        if (len > 0) {
            formatted.append("(");
            formatted.append(digits.substring(0, Math.min(3, len)));
            if (len >= 3) {
                formatted.append(") - ");
                formatted.append(digits.substring(3, Math.min(6, len)));
                if (len >= 6) {
                    formatted.append("-");
                    formatted.append(digits.substring(6));
                }
            }
        }
        return formatted.toString();
    }

    public static boolean isValidEmployeeRole(String role) {
        if (role == null) return false;
        return role.equalsIgnoreCase("wait staff") || role.equalsIgnoreCase("manager");
    }

    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        if (password.length() < 8) return false;
        if (!Pattern.compile("[A-Z]").matcher(password).find()) return false;
        if (!Pattern.compile("[a-z]").matcher(password).find()) return false;
        if (!Pattern.compile("[0-9]").matcher(password).find()) return false;
        if (!Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) return false;
        return true;
    }

    public static boolean isValidConfirmPassword(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) return false;
        return password.equals(confirmPassword) && !confirmPassword.isEmpty();
    }

}
