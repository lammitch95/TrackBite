package chooser.model;

import java.util.Map;

public class CurrentPageOptions {

    private static String currPageOption;
    private static Map<String,String> trackTableViewData;

    public static void setCurrPageOption(String value) {currPageOption = value;}

    public static String getCurrPageOption() {
        return currPageOption;
    }

    public static void setTrackTableViewData(Map<String,String> tableData){
        trackTableViewData = tableData;
    }


}
