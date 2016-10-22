package fi.tuomela.teemu.blurter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.Date;

import fi.tuomela.teemu.blurter.Models.Blurt;
import fi.tuomela.teemu.blurter.R;
import fi.tuomela.teemu.blurter.Utilities.CustomArrayAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayAdapter<Blurt> mArrayAdapter;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fab.getContext(), CreateBlurtActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ListView for our Blurts.
        final ListView mListView = (ListView) findViewById(R.id.blurtview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Blurt blurt = (Blurt) mListView.getItemAtPosition(i);
                Intent intent = new Intent(mListView.getContext(), BlurtActivity.class);
                intent.putExtra("BLURT", new Gson().toJson(blurt));
                startActivity(intent);
            }
        });

        // Custom ArrayAdapter to populate ListView.
        mArrayAdapter = new CustomArrayAdapter<>(this, R.layout.custom_list_item_1);
        mListView.setAdapter(mArrayAdapter);

        // Register to 'general' topic.
        // Node.js fcm-node can't target user segment so topics are the way
        // to send notifications to all devices.
        FirebaseMessaging.getInstance().subscribeToTopic("general");

        // Reference to Firebase database.
        database = FirebaseDatabase.getInstance().getReference();

        // Reference to all Blurts.
        DatabaseReference blurtsref = database.child("blurts");

        // Listen to changes in the database.
        blurtsref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Whenever a blurt is created, we add it to our adapter.
                Blurt blurt = dataSnapshot.getValue(Blurt.class);
                blurt.id = dataSnapshot.getKey();
                mArrayAdapter.insert(blurt, 0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed to read data: " + databaseError.getCode());
            }
        });

    }

    private void CreateBlurt(String header, String content) {
        Blurt blurt = new Blurt();
        blurt.name = header;
        blurt.content = content;
        blurt.date = new Date().toString();

        DatabaseReference blurtsref = database.child("blurts");
        blurtsref.push().setValue(blurt);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String header = data.getStringExtra("HEADER");
                String content = data.getStringExtra("CONTENT");
                CreateBlurt(header, content);
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_create_blurt) {
            Intent intent = new Intent(this, CreateBlurtActivity.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
