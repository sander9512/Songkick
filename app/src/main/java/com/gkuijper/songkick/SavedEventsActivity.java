package com.gkuijper.songkick;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class SavedEventsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView list;
    private EventAdapter adapter;
    private DBHandler handler;
    private ArrayList<Event> events;
    public static final String EVENT = "event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_events);


        handler = new DBHandler(getApplicationContext());
        list = (ListView) findViewById(R.id.saved_event_id);
        events = handler.getAllEvents();
        adapter = new EventAdapter(getApplicationContext(), getLayoutInflater(), events);
        list.setAdapter(adapter);

        list.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("listview", "clicked");
        Event e = events.get(position);
        Intent i = new Intent(getApplicationContext(), EventDetailActivity.class);
        i.putExtra(EVENT, e);
        startActivity(i);
        //finish();
    }
}
