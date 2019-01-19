package pl.edu.pja.s13227.smb.maps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShopListFragment extends ListFragment {

    private DatabaseReference database;
    private DatabaseReference shopsRef;
    private List<FavoriteShop> shops;
    private MapTabFragment map;

    public void setMap(MapTabFragment map) {
        this.map = map;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        database = FirebaseDatabase.getInstance().getReference();
        shopsRef = database.child("shops");
        shops = new ArrayList<>();
        final FavoriteShopArrayAdapter adapter = new FavoriteShopArrayAdapter(getActivity(), shops);
        adapter.setMap(map);

        shopsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FavoriteShop shop = dataSnapshot.getValue(FavoriteShop.class);
                shops.add(shop);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                FavoriteShop shop = dataSnapshot.getValue(FavoriteShop.class);
                shops.remove(shop);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        addShop(new FavoriteShop("aw4t4td23434", "f2343qrq344fw4fs", 40.0, 43.0, 25));
//        addShop(new FavoriteShop("aawdaw23234w4dwd", "f4fw4e3rw3rgthuiyufs", 30.0, 40.0, 25));
        setListAdapter(adapter);

        getActivity().findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View addShopFormView = inflater.inflate(R.layout.add_shop_form, null, false);
                final EditText latitude = addShopFormView.findViewById(R.id.latitude);
                final EditText longitude = addShopFormView.findViewById(R.id.longitude);
                final EditText name = addShopFormView.findViewById(R.id.name_input);
                final EditText description = addShopFormView.findViewById(R.id.desc_input);
                final EditText radius = addShopFormView.findViewById(R.id.radius);
                new MapUtils(getActivity()).currentPositionTask().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        latitude.setText(String.format(Locale.getDefault(), "%.2f", location.getLatitude()));
                        longitude.setText(String.format(Locale.getDefault(), "%.2f", location.getLongitude()));
                    }
                });
                new AlertDialog.Builder(getContext())
                        .setView(addShopFormView)
                        .setTitle("Add Favorite Shop")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String shopName = name.getText().toString();
                                String shopDescription = description.getText().toString();
                                String longitudeText = longitude.getText().toString();
                                String latitudeText = latitude.getText().toString();
                                String radiusText = radius.getText().toString();

                                FavoriteShop newShop = new FavoriteShop(
                                        shopName,
                                        shopDescription,
                                        Double.parseDouble(latitudeText.isEmpty() ? "0" : latitudeText),
                                        Double.parseDouble(longitudeText.isEmpty() ? "0" : longitudeText),
                                        Double.parseDouble(radiusText.isEmpty() ? "0" : radiusText)
                                );
                                addShop(newShop);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }

    private void addShop(FavoriteShop shop) {

        String key = shopsRef.push().getKey();
        shop.setKey(key);
        shopsRef.child(key).setValue(shop);
        map.drawMarker(shop);
    }
}
