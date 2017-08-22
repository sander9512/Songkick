package com.gkuijper.songkick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener, CityAPIConnector.CityAvailable {

    private Button zoek;
    private EditText edit;
    private ArrayList<City> CityArrayList;
    private ListView listview_city;
    private CityAdapter adapter;

    public static final String CITY = "city";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        CityArrayList = new ArrayList<>();
        listview_city = (ListView) findViewById(R.id.evenement_id);

        edit = (EditText) findViewById(R.id.search_id);
        zoek = (Button) findViewById(R.id.button_id);

        adapter = new CityAdapter(getApplicationContext(), getLayoutInflater(), CityArrayList);
        listview_city.setAdapter(adapter);
        listview_city.setOnItemClickListener(this);


        zoek.setOnClickListener(this);

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
        getMenuInflater().inflate(R.menu.drawer, menu);
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

        if (id == R.id.nav_event) {
            // Handle the camera action
            Intent i = new Intent(getApplicationContext(), SavedEventsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_artist) {

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        Log.i("btn", "Clicked");
        CityArrayList.clear();
        String editTextValue = String.valueOf(edit.getText());

            String[] URL = {
                    "http://api.songkick.com/api/3.0/search/locations.json?query=" + editTextValue + "&apikey=rX8RhAq6lkDw5OnK"
            };

            new CityAPIConnector(this).execute(URL);
        }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        City c = CityArrayList.get(position);
        Intent i = new Intent(getApplicationContext(), EventSearchActivity.class);
        i.putExtra(CITY, c);
        startActivity(i);
        //finish();
    }

    @Override
    public void CityAvailable(City city) {
        //voeg city toe aan array list
        Log.i("", "CityAvailable: " + city);
        CityArrayList.add(city);
        removeDuplicates();
        //lijst verversen
        Log.i("", "CityAvailable: " + city);
        adapter.notifyDataSetChanged();
    }

    private void removeDuplicates() {
        int size = CityArrayList.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if (!CityArrayList.get(j).getName().equals(CityArrayList.get(i).getName()))
                    continue;
                CityArrayList.remove(j);
                j--;
                size--;
            }
        }

    }
}
