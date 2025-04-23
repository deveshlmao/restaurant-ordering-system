package main.models;

public class MenuItem {
    private int id;
    private String name;
    private String imagePath;
    private double price;
    private String category;
    private int quantity;
    private boolean availability;

    public MenuItem(String name, String imagePath, double price, String category) {
        this.id = -1;
        this.name = name;
        this.imagePath = imagePath;
        this.price = price;
        this.category = category;
        this.quantity = 1;
        this.availability = true;
    }

    public MenuItem(int id, String name, double price, String imagePath, String category, boolean availability) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.price = price;
        this.category = category;
        this.quantity = 1;
        this.availability = availability;
    }

    public MenuItem(int id, String name, double price, String imagePath, String category, String availability) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.price = price;
        this.category = category;
        this.quantity = 1;
        this.availability = "Available".equalsIgnoreCase(availability);
    }

    public String getAvailabilityStatus() {
        return availability ? "Available" : "Unavailable";
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getImagePath() { return imagePath; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public boolean isAvailable() { return availability; }


    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void setAvailability(String availability) {
        this.availability = "Available".equalsIgnoreCase(availability);
    }

    public void incrementQuantity() { quantity++; }
    public void decrementQuantity() { if (quantity > 0) quantity--; }
}
