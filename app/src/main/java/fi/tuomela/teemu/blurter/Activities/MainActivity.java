package fi.tuomela.teemu.blurter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

import fi.tuomela.teemu.blurter.Models.Blurt;
import fi.tuomela.teemu.blurter.R;
import fi.tuomela.teemu.blurter.RetrofitServices.BlurtService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayAdapter<Blurt> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateBlurt(mArrayAdapter);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                System.out.println("clicked: " + mListView.getItemAtPosition(i).toString());
                LaunchBlurtActivity((Blurt) mListView.getItemAtPosition(i));
            }
        });

        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mArrayAdapter);

        GetBlurts(mArrayAdapter);
    }

    private void LaunchBlurtActivity(Blurt blurt) {
        Intent intent = new Intent(this, BlurtActivity.class);
        intent.putExtra("BLURT", new Gson().toJson(blurt));
        startActivity(intent);
    }

    private void GetBlurts(final ArrayAdapter<Blurt> adapter) {
        BlurtService blurtService = BlurtService.retrofit.create(BlurtService.class);
        final Call<List<Blurt>> call = blurtService.blurts();
        call.enqueue(new Callback<List<Blurt>>() {
            @Override
            public void onResponse(Call<List<Blurt>> call, Response<List<Blurt>> response) {
                adapter.clear();
                adapter.addAll((response.body()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Blurt>> call, Throwable t) {
                System.out.println("Failed to get data");
            }
        });
    }

    private void CreateBlurt(final ArrayAdapter<Blurt> adapter) {
        BlurtService blurtService = BlurtService.retrofit.create(BlurtService.class);
        Blurt blurt = new Blurt();
        blurt.setName("TestBlurtFromMobile");
        blurt.setContent("JustSomeFillerContent");
        final Call<Blurt> call = blurtService.createBlurt(blurt);
        call.enqueue(new Callback<Blurt>() {
            @Override
            public void onResponse(Call<Blurt> call, Response<Blurt> response) {
                GetBlurts(adapter);
            }

            @Override
            public void onFailure(Call<Blurt> call, Throwable t) {
                System.out.println("Failed to create Blurt");
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
