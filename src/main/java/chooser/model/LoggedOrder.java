package chooser.model;

import java.util.List;

public class LoggedOrder {

    private String id;
    private String loggedDate;
    private String loggedTime;
    private String signedBy;
    private List<CustomerOrder> orderList;

    public LoggedOrder(String id, String loggedDate, String loggedTime, String signedBy,List<CustomerOrder> orderList){
        this.id = id;
        this.loggedDate = loggedDate;
        this.loggedTime = loggedTime;
        this.signedBy = signedBy;
        this.orderList = orderList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoggedDate() {
        return loggedDate;
    }

    public void setLoggedDate(String loggedDate) {
        this.loggedDate = loggedDate;
    }

    public String getLoggedTime() {
        return loggedTime;
    }

    public void setLoggedTime(String loggedTime) {
        this.loggedTime = loggedTime;
    }

    public List<CustomerOrder> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<CustomerOrder> orderList) {
        this.orderList = orderList;
    }

    public String getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }


}
