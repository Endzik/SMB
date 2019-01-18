package pl.edu.pja.s13227.smb.maps;

import com.google.android.gms.maps.model.LatLng;

public class FavoriteShop {

    private String name;
    private LatLng coordinates;
    private double radius;
    private String description;

    public FavoriteShop() {}

    public FavoriteShop(String name, String description, double lat, double lng, double radius) {
        this.name = name;
        this.description = description;
        setCoordinates(lat, lng);
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double lat, double lng) {
        this.coordinates = new LatLng(lat, lng);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
