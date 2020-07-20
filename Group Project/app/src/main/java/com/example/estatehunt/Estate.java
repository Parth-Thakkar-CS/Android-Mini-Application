package com.example.estatehunt;

public class Estate {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageFieldName() {
        return imageFileName;
    }

    public void setimageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    private String type;
    private String location;
    private String available;
    private int price;
    private String imageId;
    private String imageFileName;
}
