package com.example.passwordmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EntryAdapter extends ArrayAdapter<Entry> {

    private Context context;
    private List<Entry> entries;

    public EntryAdapter(Context context, List<Entry> entries) {
        super(context, 0, entries);
        this.context = context;
        this.entries = entries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Entry entry = entries.get(position);

        TextView websiteTextView = view.findViewById(android.R.id.text1);
        TextView passwordTextView = view.findViewById(android.R.id.text2);

        websiteTextView.setText(entry.getWebsite());
        passwordTextView.setText(entry.getPassword());

        return view;
    }
}

