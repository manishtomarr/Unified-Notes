package com.flash.apps.noted.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.flash.apps.noted.Adapter.RecyclerViewAdapter;
import com.flash.apps.noted.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GridView extends AppCompatActivity {
    private static final String TAG = "GridView";
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mContent = new ArrayList<>();
    private FirebaseAuth fAuth;
    public final List<String> key = new ArrayList<>();
    //public final List<String> key1 = new ArrayList<>();


    private DatabaseReference ourNotesDatabase;

    private String noteId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_page);
        Log.e(TAG, "onCreate: Started");
        initNotes();


    }
    private void initNotes(){
        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null) {
            ourNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());

        }
        ourNotesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Intent newIntent = new Intent(this, CreateNote.class);
                Intent intent = getIntent();

                final String book = intent.getStringExtra("bookName");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //here is your every post
                    noteId = snapshot.getKey();
                    //key.add(noteId);

                    //String karja = ourNotesDatabase.child(noteId).child("");

                    ourNotesDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("Title") && dataSnapshot.hasChild("Content") && (dataSnapshot.child("NotebookName").getValue().toString().equals(book))) {
                                mTitle.add(dataSnapshot.child("Title").getValue().toString());
                                //if((dataSnapshot.child("NotebookName").getValue().toString())==book)
                                   // System.out.println("mmmm"+);
                                mContent.add(dataSnapshot.child("Content").getValue().toString());

                                initRecyclerView();
                                //System.out.println(mTitle);
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                //sendKey();
                //System.out.println("hello"+key);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init_recyclerview");
        RecyclerView recyclerView = findViewById(R.id.main_notes_list);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mTitle,mContent,this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


    }
    protected void onResume(){
        Log.e(TAG, "onCreate: Resumed");
        mTitle.clear();
        mContent.clear();
        super.onResume();
    }
    protected void onPause(){
        Log.e(TAG, "onCreate: paused");
        mTitle.clear();
        mContent.clear();
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_note_create, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.main_new_note_btn:

                Intent newIntent = new Intent(this, CreateNote.class);
                Intent intent = getIntent();

                String book = intent.getStringExtra("bookName");

                newIntent.putExtra("bookTitle",book);
                startActivity(newIntent);
                break;
        }

        return true;
    }

}





