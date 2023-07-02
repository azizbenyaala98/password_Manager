package com.example.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class EntryAdapter extends FirestoreRecyclerAdapter<Entry, EntryAdapter.ItemViewHolder> {

    private Context context;

    public EntryAdapter(@NonNull FirestoreRecyclerOptions<Entry> options, MainActivity mainActivity) {
        super(options);
        this.context = mainActivity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Entry model) {
        holder.siteTextView.setText(model.getWebsite());
        holder.usernameTextView.setText(model.getUsername());
        holder.passwordTextView.setText(model.getPassword());
        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context,ItemDetails.class);
            intent.putExtra("website",model.getWebsite());
            intent.putExtra("username",model.getUsername());
            intent.putExtra("password",model.getPassword());

            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            System.out.println("this is test selected view "+model.getWebsite());
            context.startActivity(intent);
        });


    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ItemViewHolder(view);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView siteTextView, usernameTextView, passwordTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            siteTextView = itemView.findViewById(R.id.site_text_view);
            usernameTextView = itemView.findViewById(R.id.username_text_view);
            passwordTextView = itemView.findViewById(R.id.password_text_view);
        }
    }
}
