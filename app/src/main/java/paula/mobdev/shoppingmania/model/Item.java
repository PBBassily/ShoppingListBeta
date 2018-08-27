package paula.mobdev.shoppingmania.model;

/**
 * POJO class for representing the building
 * unit of this application in JAVA, which
 * is the item object
 */
public class Item {
    // the id used by the DB
    private Integer id;

    private String name;

    // used to sort item by category
    private String category;

    private double price;

    // whether done or not
    private boolean checked = false;

    /**
     *constructor for creating an item
     */

    public Item(Integer id,String name, String category, double price, boolean checked) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.checked = checked;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public boolean getStatus(){
        return checked ;
    }
    public void setStatus(boolean checked){
        this.checked=checked;
    }

    public Integer getId() {
        return id;
    }

}
