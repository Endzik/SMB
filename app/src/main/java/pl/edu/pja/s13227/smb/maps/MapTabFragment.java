package pl.edu.pja.s13227.smb.maps;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapTabFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private MapUtils mapUtils;
    private Map<FavoriteShop, Marker> markers = new HashMap<>();
    private Map<FavoriteShop, Circle> circles = new HashMap<>();
    private List<Geofence> geofences = new ArrayList<>();
    GeofencingClient geofencingClient;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_map_tab, container, false);

        geofencingClient = new GeofencingClient(getContext());
        mapUtils = new MapUtils(getActivity());
        if (googleMap == null) {
            mapView = view.findViewById(R.id.mapView);
            mapView.onCreate(getArguments());
            mapView.onResume();

            MapsInitializer.initialize(getActivity());
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap gmap) {
                    googleMap = gmap;
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    FirebaseDatabase.getInstance().getReference().child("shops")
                            .addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    FavoriteShop shop = dataSnapshot.getValue(FavoriteShop.class);
                                    drawMarker(shop);
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                    FavoriteShop shop = dataSnapshot.getValue(FavoriteShop.class);
                                    Marker marker = markers.get(shop);
                                    if (marker != null) marker.remove();
                                    Circle circle = circles.get(shop);
                                    if (circle != null) circle.remove();
                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                    mapUtils.zoomToCurrentPosition(googleMap);
                }
            });
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void drawMarker(FavoriteShop shop) {

        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        if (!markers.containsKey(shop)) {
            markers.put(shop, googleMap.addMarker(new MarkerOptions()
                    .position(shop.getCoordinates())
                    .title(shop.getName())
                    .snippet(shop.getDescription())));
        }
        if (!circles.containsKey(shop)) {
            circles.put(shop, googleMap.addCircle(new CircleOptions()
                    .center(shop.getCoordinates())
                    .radius(shop.getRadius())
                    .fillColor(shadeColor)
                    .strokeColor(strokeColor)
                    .strokeWidth(8)));
        }

        Geofence geofence = new Geofence.Builder()
                .setRequestId(shop.getKey())
                .setCircularRegion(shop.getLatitude(), shop.getLongitude(), (float) (shop.getRadius() == 0.0 ? 1.0 : shop.getRadius()))
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .setLoiteringDelay(1)
                .build();
        geofences.add(geofence);
        try {
            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("GEOFENCING", "Successfully added geofence");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("GEOFENCING", "Failed to add geofence!", e);
                        }
                    });
        } catch (SecurityException e) {
            Log.e("GEOFENCING", "Error adding geofences", e);
        }
    }

    public void removeMarker(FavoriteShop shop) {
        Marker marker = markers.get(shop);
        if (marker != null) marker.remove();
        Circle circle = circles.get(shop);
        if (circle != null) circle.remove();
        geofencingClient.removeGeofences(Arrays.asList(shop.getKey()));
    }

    private GeofencingRequest getGeofencingRequest() {
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_DWELL)
                .addGeofences(geofences)
                .build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(getContext(), GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(getContext(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }
}
