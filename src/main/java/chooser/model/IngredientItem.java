package chooser.model;

public class IngredientItem {

    private String name;
    private String quantity;
    private String uom;
    private String prepDetails;

    public IngredientItem(String name, String quantity, String uom, String prepDetails){
        this.name = name;
        this.quantity = quantity;
        this.uom = uom;
        this.prepDetails = prepDetails;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getPrepDetails() {
        return prepDetails;
    }

    public void setPrepDetails(String prepDetails) {
        this.prepDetails = prepDetails;
    }

    @Override
    public String toString() {
        return name + ' ' + quantity + ' ' + uom + '[' +prepDetails + ']';
    }
}
