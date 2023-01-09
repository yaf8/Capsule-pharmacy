package com.example.capsule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.capsulepharmacy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private ArrayList<Users> user = new ArrayList<>();
    private Context context;
    public static final String USER_ID_KEY = "USER_ID";

    public UserAdapter(Context context) {this.context = context;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accounts_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtEmail.setText(user.get(position).getEmail());
        holder.txtFullName.setText(user.get(position).getFullName());
        holder.relativeAccount.setOnClickListener(v -> {

            Intent intent = new Intent(context, UserActivity.class);
            intent.putExtra(USER_ID_KEY, user.get(position).getEmail());
            context.startActivity(intent);

        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        assert user != null;
        if(firebaseUser.getPhotoUrl() != null) {
            Glide.with(context)
                    .asBitmap()
                    .load(firebaseUser.getPhotoUrl())
                    .into(holder.profileImage);
        } else {
            Glide.with(context)
                    .asBitmap()
                    .load(user.get(position).getUrl())
                    .into(holder.profileImage);
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUser(ArrayList<Users> user) {
        this.user = user;
    }

    @Override
    public int getItemCount() {

        if (user == null)
            System.out.println("getItemCount() : " + "null");
        if (user != null)
            return user.size();
        else
            return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtFullName;
        private TextView txtEmail;
        private ImageView profileImage;
        private ImageButton ImgBtnMenu;
        private RelativeLayout relativeAccount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtFullName = itemView.findViewById(R.id.txtFullName);
            profileImage = itemView.findViewById(R.id.profileImage);
            ImgBtnMenu = itemView.findViewById(R.id.ImgBtnMenu);
            relativeAccount = itemView.findViewById(R.id.relativeAccount);

        }
    }
}
