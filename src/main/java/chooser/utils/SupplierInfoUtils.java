package chooser.utils;

import java.util.*;

public class SupplierInfoUtils {

    private static final List<String> countriesList = new ArrayList<>();

    static{
        countriesList.add("Select Country");
        countriesList.add("AF - Afghanistan");
        countriesList.add("AL - Albania");
        countriesList.add("AR - Argentina");
        countriesList.add("AU - Australia");
        countriesList.add("AT - Austria");
        countriesList.add("BD - Bangladesh");
        countriesList.add("BE - Belgium");
        countriesList.add("BR - Brazil");
        countriesList.add("CA - Canada");
        countriesList.add("CL - Chile");
        countriesList.add("CN - China");
        countriesList.add("CO - Colombia");
        countriesList.add("CZ - Czech Republic");
        countriesList.add("DK - Denmark");
        countriesList.add("EG - Egypt");
        countriesList.add("FI - Finland");
        countriesList.add("FR - France");
        countriesList.add("DE - Germany");
        countriesList.add("GR - Greece");
        countriesList.add("HK - Hong Kong");
        countriesList.add("HU - Hungary");
        countriesList.add("IN - India");
        countriesList.add("ID - Indonesia");
        countriesList.add("IE - Ireland");
        countriesList.add("IL - Israel");
        countriesList.add("IT - Italy");
        countriesList.add("JP - Japan");
        countriesList.add("KE - Kenya");
        countriesList.add("KR - South Korea");
        countriesList.add("MY - Malaysia");
        countriesList.add("MX - Mexico");
        countriesList.add("NL - Netherlands");
        countriesList.add("NZ - New Zealand");
        countriesList.add("NG - Nigeria");
        countriesList.add("NO - Norway");
        countriesList.add("PK - Pakistan");
        countriesList.add("PE - Peru");
        countriesList.add("PH - Philippines");
        countriesList.add("PL - Poland");
        countriesList.add("PT - Portugal");
        countriesList.add("QA - Qatar");
        countriesList.add("RO - Romania");
        countriesList.add("RU - Russia");
        countriesList.add("SA - Saudi Arabia");
        countriesList.add("SG - Singapore");
        countriesList.add("ZA - South Africa");
        countriesList.add("ES - Spain");
        countriesList.add("SE - Sweden");
        countriesList.add("CH - Switzerland");
        countriesList.add("TH - Thailand");
        countriesList.add("TR - Turkey");
        countriesList.add("UA - Ukraine");
        countriesList.add("AE - United Arab Emirates");
        countriesList.add("GB - United Kingdom");
        countriesList.add("US - United States of America");
        countriesList.add("VN - Vietnam");

    }

    public static List<String> retrieveCountriesList(){
        return countriesList;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }

    public static boolean isValidWebsite(String website) {
        String websiteRegex = "^(https?://)?(www\\.)?[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(/.*)?$";
        return website != null && website.matches(websiteRegex);
    }


    public static boolean isValidPostalCode(String postalCode) {
        String postalRegex = "^\\d{5}(-\\d{4})?$|^[A-Za-z]\\d[A-Za-z][ -]?\\d[A-Za-z]\\d$";
        return postalCode != null && postalCode.matches(postalRegex);
    }



}
