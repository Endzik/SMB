package pl.edu.pja.s13227.smb.maps;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class FavoriteShop {

    private String key;
    private String name;
    private double latitude;
    private double longitude;
    private double radius;
    private String description;

    public FavoriteShop() {}

    public FavoriteShop(String name, String description, double lat, double lng, double radius) {
        this.name = name;
        this.description = description;
        this.latitude = lat;
        this.longitude = lng;
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getCoordinates() {
        return new LatLng(latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FavoriteShop && ((FavoriteShop) o).key.equals(this.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
