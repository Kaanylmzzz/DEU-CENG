
public class Customer {

    //Attributes
    public String customerId;
    public String customerName;
    public String date;
    public String productName;


    //Constructor
    Customer(String id,String name,String date,String product_name)
    {
        this.customerId = id;
        this.customerName = name;
        this.productName = product_name;
        this.date = date;
    }

    @Override
    public String toString() {
        return "date= " + date + ", " +
                "productName= " + productName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return customerName;
    }

    public String getProduct_name() {
        return productName;
    }
}