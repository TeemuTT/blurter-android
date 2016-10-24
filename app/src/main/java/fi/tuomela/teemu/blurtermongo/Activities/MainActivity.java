package fi.tuomela.teemu.blurtermongo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.List;

import fi.tuomela.teemu.blurtermongo.Models.Blurt;
import fi.tuomela.teemu.blurtermongo.R;
import fi.tuomela.teemu.blurtermongo.RetrofitServices.BlurtService;
import fi.tuomela.teemu.blurtermongo.Utilities.CustomArrayAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayAdapter<Blurt> mArrayAdapter;
    private Retrofit retrofit;

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

        mArrayAdapter = new CustomArrayAdapter<>(this, R.layout.custom_list_item_1);
        mListView.setAdapter(mArrayAdapter);

        FirebaseMessaging.getInstance().subscribeToTopic("general");

        // Initialize our Retrofit instance.
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetBlurts();
    }

    private void GetBlurts() {
        // Retrofit creates an implementation of our interface.
        BlurtService blurtService = retrofit.create(BlurtService.class);
        // Create the Call we wish to make.
        final Call<List<Blurt>> call = blurtService.blurts();
        // Perform the Call asynchronously.
        call.enqueue(new Callback<List<Blurt>>() {
            @Override
            public void onResponse(Call<List<Blurt>> call, Response<List<Blurt>> response) {
                mArrayAdapter.clear();
                // Response.body() contains an ArrayList of our Blurt objects,
                // which we can add to our adapter.
                mArrayAdapter.addAll((response.body()));
                mArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Blurt>> call, Throwable t) {
                Snackbar.make(findViewById(R.id.content_main), R.string.error_get_blurt, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void CreateBlurt(String header, String content) {
        BlurtService blurtService = retrofit.create(BlurtService.class);
        Blurt blurt = new Blurt(header, content);
        final Call<Blurt> call = blurtService.createBlurt(blurt);
        call.enqueue(new Callback<Blurt>() {
            @Override
            public void onResponse(Call<Blurt> call, Response<Blurt> response) {
                GetBlurts();
            }

            @Override
            public void onFailure(Call<Blurt> call, Throwable t) {
                Snackbar.make(findViewById(R.id.content_main), R.string.error_create_blurt, Snackbar.LENGTH_LONG).show();
            }
        });
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_refresh) {
            GetBlurts();
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
