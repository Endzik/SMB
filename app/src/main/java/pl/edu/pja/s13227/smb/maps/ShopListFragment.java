package pl.edu.pja.s13227.smb.maps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

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
    }

    private void addShop(FavoriteShop shop) {

        String key = shopsRef.push().getKey();
        shop.setKey(key);
        shopsRef.child(key).setValue(shop);
    }
}
