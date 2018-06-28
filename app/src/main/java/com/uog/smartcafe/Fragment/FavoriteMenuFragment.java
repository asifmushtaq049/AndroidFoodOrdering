package com.uog.smartcafe.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uog.smartcafe.R;
import com.uog.smartcafe.adapter.FavouriteAdapter;
import com.uog.smartcafe.database.Query;
import com.uog.smartcafe.entities.FavoriteObject;
import com.uog.smartcafe.util.Helper;

import java.util.List;


public class FavoriteMenuFragment extends Fragment {

    private static final String TAG = FavoriteMenuFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private Query databaseQuery;

    public FavoriteMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.favorite_menu_items));
        View view = inflater.inflate(R.layout.fragment_favorite_menu, container, false);

        databaseQuery = new Query(getActivity());
        List<FavoriteObject> favoriteObjects = databaseQuery.listFavoriteMenu();

        recyclerView = (RecyclerView)view.findViewById(R.id.favorite_menu);
        GridLayoutManager mGrid = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mGrid);
        recyclerView.setHasFixedSize(true);

        if(favoriteObjects == null){
            Helper.displayErrorMessage(getActivity(), "Your favorite list is empty");
        }else {
            FavouriteAdapter mAdapter = new FavouriteAdapter(getActivity(), favoriteObjects);
            recyclerView.setAdapter(mAdapter);
        }

        return view;
    }


}
