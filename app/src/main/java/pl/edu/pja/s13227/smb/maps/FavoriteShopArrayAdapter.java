package pl.edu.pja.s13227.smb.maps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Locale;

public class FavoriteShopArrayAdapter extends ArrayAdapter<FavoriteShop> {

    private Context context;
    private FavoriteShop[] shops;

    public FavoriteShopArrayAdapter(@NonNull Context context, @NonNull FavoriteShop[] shops) {
        super(context, -1, shops);
        this.context = context;
        this.shops = shops;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.favorite_shop_row, parent, false);
        TextView topRow = rowView.findViewById(R.id.topRow);
        TextView bottomRow = rowView.findViewById(R.id.bottomRow);
        FavoriteShop shop = shops[position];
        String topText = String.format(Locale.getDefault(), "%s (%.2f, %.2f), radius: %.2f",
                shop.getName(),
                shop.getCoordinates().latitude,
                shop.getCoordinates().longitude,
                shop.getRadius());
        topRow.setText(topText);
        bottomRow.setText(shop.getDescription());
        return rowView;
    }
}
