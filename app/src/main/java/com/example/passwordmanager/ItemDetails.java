package com.example.passwordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ItemDetails extends AppCompatActivity {
    EditText SiteEditText,EmailEditText,PasswordEditText;
    TextView pageTitleTextView;
    ImageButton saveItemBtn;
    Button deleteNoteTextViewBtn ;
    private FirebaseAuth auth;private FirebaseFirestore db;


    private CollectionReference entriesCollection;private List<Entry> entries;
    private EntryAdapter entryAdapter;
    String website,username,password,docId;
    boolean isEditMode = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        SiteEditText = findViewById(R.id.notes_site_text);
        EmailEditText = findViewById(R.id.notes_email_text);
        PasswordEditText= findViewById(R.id.notes_passsword_text);
        pageTitleTextView=findViewById(R.id.page_title);
        deleteNoteTextViewBtn=findViewById(R.id.delete_note_text_view_btn);

        saveItemBtn= findViewById(R.id.save_item_btn);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        entriesCollection = db.collection("entries");

        website = getIntent().getStringExtra("website");
        username= getIntent().getStringExtra("username");
        password= getIntent().getStringExtra("password");
        docId = getIntent().getStringExtra("docId");




        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        SiteEditText.setText(website);
        EmailEditText.setText(username);
        PasswordEditText.setText(password);

        if(isEditMode){
            pageTitleTextView.setText("Edit ! ");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        saveItemBtn.setOnClickListener( (v)-> saveNote());

       deleteNoteTextViewBtn.setOnClickListener((v)-> deleteNoteFromFirebase() );

    }

    void saveNote(){
        String site = SiteEditText.getText().toString();
        String username = EmailEditText.getText().toString();
        String password = PasswordEditText.getText().toString();


        if(site==null || site.isEmpty() ){
            SiteEditText.setError("website  is required");
            return;
        }
        Entry entry = new Entry();
        entry.setWebsite(site);
        entry.setUsername(username);
        entry.setPassword(password);

        saveNoteToFirebase(entry);

    }

    void saveNoteToFirebase(Entry entry){
        DocumentReference documentReference;
        if(isEditMode){
            //update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }else{
            //create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }



        documentReference.set(entry).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is added
                    Utility.showToast(ItemDetails.this," added successfully");
                    finish();
                }else{
                    Utility.showToast(ItemDetails.this,"Failed while adding ");
                }
            }
        });

    }

    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(ItemDetails.this," deleted successfully");
                    finish();
                }else{
                    Utility.showToast(ItemDetails.this,"Failed while deleting ");
                }
            }
        });
    }








    }

