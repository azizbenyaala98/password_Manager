package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;




import android.content.Intent;

import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView userName;

    Button logout;
    private EditText websiteEditText;
    private EditText passwordEditText;
    private Button submitPasswordButton;
    private ListView entriesListView;
    private List<Entry> entries;
    private EntryAdapter entryAdapter;
    private FirebaseFirestore db;
    private CollectionReference entriesCollection;

    ;
    private Button addBtn;







    private FirebaseAuth auth;

    private String userId;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        logout = findViewById(R.id.logout);
        userName = findViewById(R.id.userName);
        String displayemail = getIntent().getStringExtra("name");
        String displayName=getCharactersBeforeAt(displayemail);
        websiteEditText = findViewById(R.id.website);
        passwordEditText = findViewById(R.id.password);
        addBtn = findViewById(R.id.addBtn);
        userName = findViewById(R.id.userName);
        entriesListView = findViewById(R.id.listview);
        entries = new ArrayList<>();
        entryAdapter = new EntryAdapter(this, entries);
        entriesListView.setAdapter(entryAdapter);
        auth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        entriesCollection = db.collection("entries");

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            String name = currentUser.getDisplayName();
            userName.setText(name);
        }

        userName.setText(displayName);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 {

                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                }});
        addBtn.setOnClickListener(view -> {
            String website = websiteEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!website.isEmpty() && !password.isEmpty()) {
                Entry newEntry = new Entry(website, password, userId);

                entriesCollection.add(newEntry)
                        .addOnSuccessListener(documentReference -> {
                            newEntry.setId(documentReference.getId());
                            entries.add(newEntry);
                            entryAdapter.notifyDataSetChanged();

                            websiteEditText.setText("");
                            passwordEditText.setText("");
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(MainActivity.this, "Failed to add entry.", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(MainActivity.this, "Please enter both website and password", Toast.LENGTH_SHORT).show();
            }
        });

        entriesCollection.whereEqualTo("userId", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("MainActivity", "Error getting entries: ", error);
                        return;
                    }

                    entries.clear();
                    if (value != null) {
                        for (DocumentSnapshot doc : value) {
                            Entry entry = doc.toObject(Entry.class);
                            entry.setId(doc.getId());
                            entries.add(entry);
                        }
                    }

                    entryAdapter.notifyDataSetChanged();
                });





}public  String getCharactersBeforeAt(String email) {
        int atIndex = email.indexOf("@");

        if (atIndex != -1) {
            return email.substring(0, atIndex);
        }

        return null;
    }
}
