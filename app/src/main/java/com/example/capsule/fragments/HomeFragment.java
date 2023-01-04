package com.example.capsule.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capsule.ProductAdapter;
import com.example.capsule.TempActivity;
import com.example.capsule.Utils;
import com.example.capsulepharmacy.R;

public class HomeFragment extends Fragment {

    public static final String PRODUCT_ID_KEY = "productID";

    private RecyclerView recycleVerticalItems;
    private ProductAdapter adapter;
    private ImageButton imgBtnSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recycleVerticalItems = view.findViewById(R.id.recycleVerticalItems);
        imgBtnSearch = view.findViewById(R.id.imgBtnSearch);


        adapter = new ProductAdapter(getActivity());
        adapter.setProduct(Utils.getInstance().getAllProducts());

        recycleVerticalItems.setAdapter(adapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 2000);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recycleVerticalItems.setLayoutManager(linearLayoutManager);


        imgBtnSearch.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), TempActivity.class));
        });


        return view;
    }

}