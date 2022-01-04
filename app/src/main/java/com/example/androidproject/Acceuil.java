package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Accueil extends AppCompatActivity {
    RecyclerView recview;
    myadapter adapter;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        // getSupportActionBar().setTitle(Html.fromHtml("<front color=\"red\">" +getString(R.string.app_name) + "</font>"));
        //button = findViewById(R.id.nouveau);
/*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

        });
*/

        recview=findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Quad> options =
                new FirebaseRecyclerOptions.Builder<Quad>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("quad"), Quad.class)
                        .build();

        adapter=new myadapter(options,getApplicationContext());
        recview.setAdapter(adapter);


    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {   // Inflate the menu;
        //this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        Intent i = new Intent(getApplicationContext(),MainActivity.class);


        switch (item.getItemId()) {
            case R.id.ajouter: startActivity(i);
                break;


            //case R.id.quitte: finish();
        }

        Intent j = new Intent(getApplicationContext(),Map.class);


        switch (item.getItemId()) {
            case R.id.map: startActivity(j);
                break;


            //case R.id.quitte: finish();
        }

        Intent k = new Intent(getApplicationContext(),login.class);


        switch (item.getItemId()) {

            case R.id.quite: startActivity(k);
                FirebaseAuth.getInstance().signOut();
                break;


            //case R.id.quitte: finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem item=menu.findItem(R.id.search);

        SearchView searchView=(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void processsearch(String s)
    {
        FirebaseRecyclerOptions<Quad> options =
                new FirebaseRecyclerOptions.Builder<Quad>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("quad").orderByChild("marque").startAt(s).endAt(s+"\uf8ff"), Quad.class)
                        .build();

        adapter=new myadapter(options,getApplicationContext());
        adapter.startListening();
        recview.setAdapter(adapter);

    }

}





