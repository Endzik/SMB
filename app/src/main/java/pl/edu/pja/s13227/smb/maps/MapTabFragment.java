package pl.edu.pja.s13227.smb.maps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapTabFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private MapUtils mapUtils;

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

                    FavoriteShop shop = new FavoriteShop(
                            "Test Name",
                            "Test description",
                            53.551, 9.993,
                            50.0);

                    drawMarker(googleMap, shop);

                    mapUtils.zoomToCurrentPosition(googleMap);
                    //fill markers
                    //add marker listener


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

        googleMap.addMarker(new MarkerOptions()
                .position(shop.getCoordinates())
                .title(shop.getName())
                .snippet(shop.getDescription()));

        googleMap.addCircle(new CircleOptions()
                .center(shop.getCoordinates())
                .radius(shop.getRadius())
                .fillColor(shadeColor)
                .strokeColor(strokeColor)
                .strokeWidth(8));
    }
}
