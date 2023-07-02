package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;




import android.content.Intent;

import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EntryAdapter entryAdapter;
    TextView userName;
    Button logout;

    private FirebaseFirestore db;
    private CollectionReference entriesCollection;
    private CollectionReference entryCollection;

    FloatingActionButton addBtn;
    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // logout and display system
        logout = findViewById(R.id.logout);
        userName = findViewById(R.id.userName);
        String displayemail = getIntent().getStringExtra("name");
        String displayName = getCharactersBeforeAt(displayemail);
        addBtn = findViewById(R.id.add_item_btn);
        recyclerView=findViewById(R.id.recyler_view);

        // firebase intitation
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        entriesCollection = db.collection("entries");
        FirebaseUser currentUser = auth.getCurrentUser();
        // to display name of current user
        if (currentUser != null) {
            userId = currentUser.getUid();
            String name = currentUser.getDisplayName();
            userName.setText(name);
        }
        userName.setText(displayName);
        setupRecyclerView();


// logout button logic
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {

                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });
////////////////////end logout button logic ////////////////////////////////

        //// recycler view display logic //////////

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {

                    startActivity(new Intent(MainActivity.this, ItemDetails.class));
                }
            }
        });
    }


public  String getCharactersBeforeAt(String email) {
        int atIndex = email.indexOf("@");

        if (atIndex != -1) {
            return email.substring(0, atIndex);
        }

        return null;
    }
    void setupRecyclerView(){
        Query query  = Utility.getCollectionReferenceForNotes().orderBy("website",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Entry> options = new FirestoreRecyclerOptions.Builder<Entry>()
                .setQuery(query,Entry.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        entryAdapter = new EntryAdapter(options,this);
        recyclerView.setAdapter(entryAdapter);
    }


///       end recycler view display logic /////////

    @Override
    protected void onStart() {
        super.onStart();
        entryAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        entryAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        entryAdapter.notifyDataSetChanged();
    }

}
