package com.flash.apps.noted.Activity;


import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationManager;
import android.location.*;

import com.flash.apps.noted.R;

import java.io.IOException;
import java.util.*;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity   {
    DatabaseHelper myDB;
    ImageButton addNotebook;
    RecyclerViewAdapterBook myAdapter;
    SearchView search;
    TextView location;
    private LocationManager locationManager;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws IllegalStateException {
        myDB = new DatabaseHelper( this );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        requestSelfPermission();
        locationManager = (LocationManager) getSystemService( getApplicationContext().LOCATION_SERVICE );

        //search = (SearchView) findViewById( R.id.searchID );
        addNotebook =  findViewById( R.id.addiconID );
        addNotebook.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpEditText();
            }
        } );
        updateNoteBooks();
    }

    private void requestSelfPermission() {
        if (ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION )
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1 );
        }
    }

    private ArrayList<Book> fetchNotebooks(Cursor cursor) {
        ArrayList<Book> list_books = new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String str1, str2;
            str1 = cursor.getString( 0 );
            str2 = cursor.getString( 1 );
            list_books.add( new Book( str1, "Newark" ) );
            cursor.moveToNext();
        }
        return list_books;
    }

    public void updateNoteBooks() {
        Cursor cursor = myDB.getAllData();
        ArrayList<Book> list_book = fetchNotebooks(cursor);
        RecyclerView myrv =  findViewById(R.id.recyclerview_id);
        myAdapter = new RecyclerViewAdapterBook(MainActivity.this, list_book);
        myrv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        myrv.setAdapter(myAdapter);
    }

    public void updateOnSearch(){
        final String input = search.toString();
        Cursor cursor = myDB.search(input);
        ArrayList<Book> list_book = fetchNotebooks(cursor);
        RecyclerView myrv = findViewById(R.id.recyclerview_id);
        myAdapter = new RecyclerViewAdapterBook(MainActivity.this, list_book);
        myrv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        myrv.setAdapter(myAdapter);
    }

    private void popUpEditText() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("NoteBook");

        final EditText input1 = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input1.setLayoutParams(lp);
        builder.setView(input1);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String input = input1.getText().toString();
                final String location = getLastKnownLocation();
                if (input.equals(""))
                    Toast.makeText(getApplicationContext(), "Input not filled", Toast.LENGTH_SHORT).show();
                else {
                    myDB.insertData(input,"Newark");
                    updateNoteBooks();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private String getLastKnownLocation() {
        List<String> providers = locationManager.getProviders( true );
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location l = locationManager.getLastKnownLocation( provider );
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        if(bestLocation!=null){
            longitude = bestLocation.getLongitude();
            latitude = bestLocation.getLatitude();
        }

        if(bestLocation!=null)
        return "Newark";
        else
            return null;
    }

    private String locationToString(Location bestLocation) {
        String city="";
        Geocoder mGeoCoder = new Geocoder( this ,Locale.getDefault());
        List<Address> addressList = null;
        try {
            addressList = mGeoCoder.getFromLocation(bestLocation.getLatitude(),bestLocation.getLongitude(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        city = addressList.get( 0 ).getLocality();
        return city;
    }


}
