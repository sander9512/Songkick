package com.gkuijper.songkick;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import static com.gkuijper.songkick.EventSearchActivity.EVENT;

public class EventDetailActivity extends AppCompatActivity {
    private TextView name, type, venue_city, datetime;
    private ListView detail;
    private Event event;
    private PerformanceAdapter adapter;
    private ArrayList performances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Bundle bundle = getIntent().getExtras();
        event = (Event) bundle.get(EVENT);
        performances = event.getPerformances();

        name = (TextView) findViewById(R.id.name_id);
        type = (TextView) findViewById(R.id.type_id);
        venue_city = (TextView) findViewById(R.id.venue_city_id);
        datetime = (TextView) findViewById(R.id.date_time_id);
        detail = (ListView) findViewById(R.id.event_detail_id);

        name.setText(event.getName());
        type.setText(event.getType());
        venue_city.setText(event.getVenue() + " " + event.getCity().getCountry());
        Log.i("City", event.getCity().getName().toString());
        datetime.setText("Van " + event.getStartdate() + " " + event.getTime() + " tot " + event.getEnddate());

        adapter = new PerformanceAdapter(getApplicationContext(), getLayoutInflater(), performances);
        detail.setAdapter(adapter);
        Log.i("ID", event.getEventID().toString());


    }
}
