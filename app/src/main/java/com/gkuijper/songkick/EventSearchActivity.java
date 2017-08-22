package com.gkuijper.songkick;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import static com.gkuijper.songkick.DrawerActivity.CITY;

public class EventSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, EventAPIConnector.EventAvailable {
    private int id = 0;
    private City city;
    private Button zoek;
    private EditText edit;
    private ArrayList<Event> EventArrayList, resultList;
    private ListView list;
    private EventAdapter adapter;
    private boolean searched = false;
    public static final String EVENT = "event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search);
        list = (ListView) findViewById(R.id.artists_row_id);

        zoek = (Button) findViewById(R.id.button_artist_id);
        edit = (EditText) findViewById(R.id.search_artist__id);

        Bundle bundle = getIntent().getExtras();
        city = (City) bundle.get(CITY);

        EventArrayList = new ArrayList<>();
        resultList = new ArrayList<>();

        adapter = new EventAdapter(this, getLayoutInflater(), EventArrayList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        zoek.setOnClickListener(this);
        zoek.clearFocus();
        list.requestFocus();
        id = city.getId();
        getEvents();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("listview", "clicked");
        Event e;
        if (searched) {
            e = resultList.get(position);
        } else {
            e = EventArrayList.get(position);
        }
        Intent i = new Intent(getApplicationContext(), EventDetailActivity.class);
        i.putExtra(EVENT, e);
        startActivity(i);
        //finish();

    }

    @Override
    public void onClick(View v) {
        resultList.clear();
        String artistname = String.valueOf(edit.getText());
        Log.i("btnclick2", artistname);

        for (int i = 0; i < EventArrayList.size(); i++) {
            ArrayList<Performance> performances  = EventArrayList.get(i).getPerformances();
            for (int j = 0; j < performances.size(); j++) {
                if (performances.get(j).getName().toLowerCase().contains(artistname.toLowerCase())) {
                    Log.i("Performance", performances.get(j).getName().toLowerCase().toString());
                    resultList.add(EventArrayList.get(i));
                }
            }
        }
        Log.i("events", resultList.toString());

        EventAdapter adapter2 = new EventAdapter(getApplicationContext(), getLayoutInflater(), resultList);
        list.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
        searched = true;



    }

    public void getEvents() {
        Log.i("btn", "Clicked");
        String[] URL = {
                "http://api.songkick.com/api/3.0/events.json?apikey=rX8RhAq6lkDw5OnK&location=sk:" + id
        };

          new EventAPIConnector(this).execute(URL);

    }

    @Override
    public void onEventAvailable(Event event) {
        event.setCity(city);
        EventArrayList.add(event);
        removeDuplicates();
        adapter.notifyDataSetChanged();

    }

    private void removeDuplicates() {
        int size = EventArrayList.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if (!EventArrayList.get(j).getName().equals(EventArrayList.get(i).getName()))
                    continue;
                EventArrayList.remove(j);
                j--;
                size--;
            }
        }

    }
}
