package pl.edu.pja.s13227.smb.maps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MapTabFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private MapUtils mapUtils;
    private Map<FavoriteShop, Marker> markers = new HashMap<>();
    private Map<FavoriteShop, Circle> circles = new HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_map_tab, container, false);

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
                            drawMarker(googleMap, shop);
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

    private void drawMarker(GoogleMap googleMap, FavoriteShop shop) {

        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        markers.put(shop, googleMap.addMarker(new MarkerOptions()
                .position(shop.getCoordinates())
                .title(shop.getName())
                .snippet(shop.getDescription())));

        circles.put(shop, googleMap.addCircle(new CircleOptions()
                .center(shop.getCoordinates())
                .radius(shop.getRadius())
                .fillColor(shadeColor)
                .strokeColor(strokeColor)
                .strokeWidth(8)));
    }

    public void removeMarker(FavoriteShop shop) {
        Marker marker = markers.get(shop);
        if (marker != null) marker.remove();
        Circle circle = circles.get(shop);
        if (circle != null) circle.remove();
    }
}
