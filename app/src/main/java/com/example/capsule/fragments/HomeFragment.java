package com.example.capsule.fragments;

import static android.content.ContentValues.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capsule.ProductAdapter;
import com.example.capsule.Utils;
import com.example.capsulepharmacy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    public static final String PRODUCT_ID_KEY = "productID";

    private RecyclerView recycleVerticalItems;
    private ProductAdapter adapter;
    private ImageButton imgBtnSearch;
    private ImageView capsuleLogo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recycleVerticalItems = view.findViewById(R.id.recycleVerticalItems);
        imgBtnSearch = view.findViewById(R.id.imgBtnSearch);
        capsuleLogo = view.findViewById(R.id.capsuleLogo);


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
            //startActivity(new Intent(getActivity(), TempActivity.class));

            newNotification();
        });

        capsuleLogo.setOnClickListener(v -> adapter.notifyDataSetChanged());


        newNotification();


        return view;
    }

    private void newNotification() {

        //Write
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("order/messages/med");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String value = dataSnapshot.getValue(String.class);

                createNotificationChannel();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "CHANNEL_ID")
                        .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.capsule_logo))
                        .setContentTitle("New order")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(value))
                        .setPriority(NotificationCompat.PRIORITY_MAX);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());

                notificationManager.notify(0, builder.build());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }


    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "New Order";
            String description = "Order Notification!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel("CHANNEL_ID", name, importance);
            notificationChannel.setDescription(description);
            notificationChannel.setLightColor(Color.RED);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }


}