package com.flash.apps.noted.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.lang.*;

import com.flash.apps.noted.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateNote extends AppCompatActivity {

    private EditText etTitle, etContent;
    //private Toolbar mToolbar;
    public FirebaseAuth fAuth;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    public ImageView mImageView;
    String title;
    String content;
    public String noteID = "";
    public String position;
    public String id;
    private List<String> key = new ArrayList<>() ;
    //public String position;
    //public int ID=0;
    // public Uri mImageUri;
    private DatabaseReference ourNoteDatabase;
    private boolean isExist;

    private String slectedText;
    private String selectedText;
    String formatted = null;
    private EditText edit ;
    private StorageReference mStorageReference;
    public Uri mImageUri;
    public Uri fileUri;


    public void getTypeface(int id) {
        edit = (EditText) findViewById( R.id.editID ) ;
        Spanned text = Html.fromHtml( slectedText );
        edit.setText(text.toString());
        if(edit.getTypeface() !=null){
            if(slectedText.contains( "<b>" )){
                SpannableStringBuilder sb = new SpannableStringBuilder(text);
                sb.setSpan( new StyleSpan( Typeface.NORMAL ),0,sb.length()-1,0 );
                formatted = String.valueOf( sb.subSequence(0,sb.length()) );
            }else if (slectedText.contains( "<i>")) {
                SpannableStringBuilder sb = new SpannableStringBuilder(text);
                sb.setSpan( new StyleSpan( Typeface.NORMAL ),0,sb.length()-1,0 );
                formatted = String.valueOf( sb.subSequence(0,sb.length()) );
            }else if (slectedText.contains( "<u>")) {
                SpannableStringBuilder sb = new SpannableStringBuilder(text);
                sb.setSpan( new StyleSpan( Typeface.NORMAL ),0,sb.length()-1,0 );
                formatted = String.valueOf( sb.subSequence(0,sb.length()) );
            }
            else
            {
                if (id == R.id.action_bold) {
                    formatted = "<b>" + slectedText + "</b>";
                }
                if (id == R.id.action_italic) {
                    formatted = "<i>" + slectedText + "</i>";
                }
                if (id == R.id.action_underline) {
                    formatted = "<u>" + slectedText + "</u>";
                }

            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_note);
        fAuth = FirebaseAuth.getInstance();
        ourNoteDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
        etTitle =  findViewById(R.id.etTitle);
        etContent =  findViewById(R.id.etContent);
        mImageView = findViewById(R.id.image_CreateNote);
        //System.out.println("hello");

        etContent.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.items, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                int id = item.getItemId();
                int selStart = 0;
                int selEnd = 0;
                int min = 0;
                int max = etContent.getText().toString().length();

                if (etContent.isFocused()) {
                    selStart = etContent.getSelectionStart();
                    selEnd = etContent.getSelectionEnd();
                    if (selStart > selEnd) {
                        selEnd = etContent.getSelectionStart();
                        selStart = etContent.getSelectionEnd();
                    }
                }
                //selectedText = (String) editText.getText().subSequence( selStart, selEnd );

                String textBefore = Html.toHtml( new SpannableString( etContent.getText().subSequence( 0, selStart ) ) );
                slectedText = Html.toHtml( new SpannableString( etContent.getText().subSequence( selStart, selEnd ) ) );
                String textAfter = Html.toHtml( new SpannableString( etContent.getText().subSequence( selEnd, max ) ) );

                if (!slectedText.equals( "" ) || selStart != selEnd) {
                    getTypeface(id);
                    StringBuilder builder = new StringBuilder();
                    builder.append( textBefore );
                    builder.append( formatted );
                    builder.append( textAfter );

                    etContent.setText( Html.fromHtml( builder.toString() ) );
                    etContent.setSelection( selEnd );
                    textBefore = "";
                    textAfter = "";
                    slectedText = "";
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        try {
            //GridView gridView = new GridView();
            position = getIntent().getStringExtra("noteId");
            final int pos = Integer.parseInt(position);
            isExist = false;
            ourNoteDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //here is your every post
                        id = snapshot.getKey();
                        key.add(id);
                    }
                    System.out.println("hello2"+key);
                    noteID = key.get(pos);
                    System.out.println("hello1"+noteID);
                    if (!noteID.trim().equals("")) {
                        isExist = true;
                        putData();
                        key.clear();
                    } else {
                        isExist = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }


        //mToolbar = (Toolbar) findViewById(R.id.new_Note_Toolbar);
        //setSupportActionBar(mToolbar);

    }



    @Override
    public void onBackPressed() {

        title = etTitle.getText().toString().trim();
        content = etContent.getText().toString().trim();
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            createNote(title, content);
        } else {
            Toast.makeText(CreateNote.this, "Fill empty fields" , Toast.LENGTH_SHORT).show();
        }

        super.onBackPressed();

    }

    public void putData(){
        isExist = true;
        if (isExist) {
            System.out.println("hello"+noteID);
            ourNoteDatabase.child(noteID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("Title") && dataSnapshot.hasChild("Content")) {
                        String title = dataSnapshot.child("Title").getValue().toString();
                        String content = dataSnapshot.child("Content").getValue().toString();

                        etTitle.setText(title);
                        etContent.setText(content);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    public void createNote (String title, String content) {
        //ImageDisplay image = new ImageDisplay();
        //String text = image.fileName();
        //String url = image.fileUrl();
        //Upload upload = new Upload(text,url);
        MainActivity ma = new MainActivity();
        Intent intent = getIntent();
        String bookName = intent.getStringExtra("bookTitle");
        if(bookName == null)
            System.out.print("BookName Updated"+ bookName);
        if (fAuth.getCurrentUser() != null) {
            if (isExist) {
                Map updateMap = new HashMap();
                updateMap.put("NotebookName", bookName );
                updateMap.put("Title", etTitle.getText().toString().trim());
                updateMap.put("Content", etContent.getText().toString().trim());
                updateMap.put("Timestamp", ServerValue.TIMESTAMP);
                //updateMap.put("Image", upload);

                ourNoteDatabase.child(noteID).updateChildren(updateMap);

                Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();

            } else {
                final DatabaseReference newNoteRef = ourNoteDatabase.push();
                // noteID = newNoteRef.getKey();
                final Map noteMap = new HashMap();
                noteMap.put("NotebookName", bookName );

                noteMap.put("Title", title);
                noteMap.put("Content", content);
                noteMap.put("Timestamp", ServerValue.TIMESTAMP);
                //noteMap.put("Image", upload);

                newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Snackbar.make(findViewById(R.id.NoteCreation), "Note Added", Snackbar.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(CreateNote.this, "Error adding to database", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }else{
            Toast.makeText(this, "USER IS NOT SIGNED IN", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_new_image, menu);
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        getMenuInflater().inflate(R.menu.menu_audio, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.main_new_image_btn:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
                break;
            case R.id.main_camera_btn:
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");
                i.putExtra("data",f.toString());
                startActivityForResult(i, CAMERA_REQUEST_CODE);
                break;
            case R.id.main_new_audio_btn:
                Intent intent1 = new Intent(this,audio_getter.class);
                startActivity(intent1);
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.e(TAG,"ActivityResult: Started");

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(mImageView);
        }else if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            // String extra = getIntent().getExtras("data");
            System.out.println("gaanabaja"+fileUri);
            Picasso.with(this).load(fileUri).into(mImageView);
        }
    }
    private String getFileExtension(Uri uri){
        //Log.e(TAG,"FileExtension: Started");
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile(){
        //Log.e(TAG,"UploadFile: Started");

        if (mImageUri != null) {
            final StorageReference fileReference = mStorageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    //hoja = task.getResult().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }else {
            Toast.makeText(this,"No File Selected",Toast.LENGTH_SHORT).show();
        }
        //System.out.println("gaana "+ hoja);
    }
}