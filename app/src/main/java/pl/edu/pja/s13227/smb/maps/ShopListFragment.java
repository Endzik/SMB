package pl.edu.pja.s13227.smb.maps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ShopListFragment extends ListFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FavoriteShop[] shops = new FavoriteShop[] {
                new FavoriteShop(
                    "Test Name",
                    "Test description",
                    53.551, 9.993,
                    50.0),
                new FavoriteShop(
                    "Test Name2",
                    "Test description2",
                    54.551, 8.993,
                    25.0)
        };

        ArrayAdapter<FavoriteShop> adapter = new FavoriteShopArrayAdapter(getActivity(), shops);
        setListAdapter(adapter);
    }

}
