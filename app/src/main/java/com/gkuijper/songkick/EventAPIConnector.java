package com.gkuijper.songkick;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Gabrielle on 20-08-17.
 */

public class EventAPIConnector extends AsyncTask<String, Void, String>{
    //tag to use when information is logged, shows class name
    private static final String TAG = EventAPIConnector.class.getSimpleName();

    private EventAPIConnector.EventAvailable listener = null;

    public EventAPIConnector(EventAPIConnector.EventAvailable listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        //create string of bytes
        InputStream inputStream = null;
        //result we will return
        String response = "";
        int responseCode = -1;
        //create URL object with given URL as parameter in .execute()
        String productUrl = params[0];
        try {
            URL url = new URL(productUrl);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                return null;
            }
            //initialise HTTP connection
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");

            //request connection using given URL
            httpConn.connect();

            //check if succeeded
            responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
                response = getStringFromInputStream(inputStream);
            } else {
                Log.e(TAG, "Error, invalid response");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground | MalformedURLException " + e.getLocalizedMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground | IOException" + e.getLocalizedMessage());
        }
        Log.i("API", response);
        return response;
    }


    @Override
    protected void onPostExecute(String response) {
        //check if response is valid
        if (response == null || response == "") {
            Log.e(TAG, "onPostExecute received empty response" + response);
            return;
        }

        //JSON parsing
        JSONObject json;

        try {
            //entire JSON file in JSONObject
            json = new JSONObject(response);

            //get all items
            JSONObject resultpage = json.getJSONObject("resultsPage");


            //for (int i = 0; i < resultpage.length(); i++) {
               // Event e = new Event();
                //ArrayList<Performance> performanceArray = new ArrayList<>();

                JSONObject results = resultpage.getJSONObject("results");

                //for (int v = 0; v < results.length(); v++) {
                    JSONArray events = results.getJSONArray("event");

                    for (int u = 0; u < events.length(); u++) {
                        Event e = new Event();
                        ArrayList<Performance> performanceArray = new ArrayList<>();

                        JSONObject eventdetails = events.getJSONObject(u);
                        String type = eventdetails.optString("type");
                        String name = eventdetails.optString("displayName");
                        JSONObject venuedetails = events.getJSONObject(u).getJSONObject("venue");
                        String venue = venuedetails.optString("displayName");
                        //for (int p = 0; p < venuedetails.length(); p++) {
                        //    venue =
                        //}
                        JSONObject startdetails = events.getJSONObject(u).getJSONObject("start");
                        String startdateString = startdateString = startdetails.optString("date");
                        String startTimeString = startTimeString =  " "+startdetails.optString("time");
                        //for (int k = 0; k < startdetails.length(); k++) {
                        //
                        //    ;
                        //}
                        String enddateString = null;
                        if(events.getJSONObject(u).optJSONObject("end") != null){
                            JSONObject enddetails = events.getJSONObject(u).getJSONObject("end");
                            enddateString = enddetails.optString("date");

                        } else{
                            enddateString = "N/A";
                        }
                        String id = eventdetails.optString("id");

                        //JSONArray performances = events.getJSONObject(u).getJSONArray("performance");
                        JSONArray performances = eventdetails.getJSONArray("performance");

                        for (int y = 0; y < performances.length(); y++) {

                            JSONObject performance = performances.getJSONObject(y);
                            String artist = performance.optString("displayName");
                            String billing = performance.optString("billing");

                            Performance p = new Performance(artist, billing);
                            performanceArray.add(p);
                        }

                        Log.i("performances", performanceArray.toString());
                        Event event = new Event(name, startTimeString, type, venue, startdateString, enddateString, performanceArray, id);
                        listener.onEventAvailable(event);

                    }

             //   }
            //}

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }


    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    //call back interface
    public interface EventAvailable {
        void onEventAvailable(Event event);
    }
}
