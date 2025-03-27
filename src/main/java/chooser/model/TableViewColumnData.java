package chooser.model;

public class TableViewColumnData {

    private String columnID;
    private String columnName;
    private String[] columnData;

    public TableViewColumnData(String id, String name, String[] dataArr){
        this.setColumnID(id);
        this.setColumnName(name);
        this.setColumnData(dataArr);
    }


    public String getColumnID() {
        return columnID;
    }

    public void setColumnID(String columnID) {
        this.columnID = columnID;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String[] getColumnData() {
        return columnData;
    }

    public void setColumnData(String[] columnData) {
        this.columnData = columnData;
    }
}
