package pl.edu.pja.s13227.smb.maps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

public class FavoriteShopArrayAdapter extends ArrayAdapter<FavoriteShop> {

    private Context context;
    private List<FavoriteShop> shops;
    private DatabaseReference shopsRef;
    private MapTabFragment map;

    public FavoriteShopArrayAdapter(@NonNull Context context, @NonNull List<FavoriteShop> shops) {
        super(context, -1, shops);
        this.context = context;
        this.shops = shops;
        shopsRef = FirebaseDatabase.getInstance().getReference().child("shops");
    }

    public void setMap(MapTabFragment map) {
        this.map = map;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.favorite_shop_row, parent, false);
        TextView topRow = rowView.findViewById(R.id.topRow);
        TextView bottomRow = rowView.findViewById(R.id.bottomRow);
        final FavoriteShop shop = shops.get(position);
        String topText = String.format(Locale.getDefault(), "%s (%.2f, %.2f), radius: %.2f",
                shop.getName(),
                shop.getCoordinates().latitude,
                shop.getCoordinates().longitude,
                shop.getRadius());
        topRow.setText(topText);
        bottomRow.setText(shop.getDescription());
        Button deleteButton = rowView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopsRef.child(shop.getKey()).removeValue();
                shops.remove(shop);
                notifyDataSetChanged();
                map.removeMarker(shop);
            }
        });
        return rowView;
    }
}
