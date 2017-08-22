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
 * Created by Gabrielle on 06-08-17.
 */

public class CityAPIConnector extends AsyncTask<String, Void, String> {
    //tag to use when information is logged, shows class name
    private static final String TAG = CityAPIConnector.class.getSimpleName();

    private CityAvailable listener = null;

    public CityAPIConnector(CityAvailable listener) {
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

            String cityname = null;
            int id = 0;
            String country = null;
            for (int i = 0; i < resultpage.length(); i++) {

                JSONObject results = resultpage.getJSONObject("results");
                System.out.println("After i: " + resultpage.length() + " results l: " + results.length());

                //for (int v = 0; v < results.length(); v++) {
                    JSONArray locations = results.getJSONArray("location");

                    for (int u = 0; u < locations.length(); u++) {
                        JSONObject citynames = locations.getJSONObject(u).getJSONObject("city");

                        cityname = citynames.optString("displayName");
                    }


                    for (int p = 0; p < locations.length(); p++) {
                        JSONObject cities = locations.getJSONObject(p).getJSONObject("metroArea");

                        for (int m = 0; m < cities.length(); m++) {
                            //JSONObject city = cities.getJSONObject(m);
                            id = cities.optInt("id");

                            JSONObject countrynames = cities.getJSONObject("country");

                            for (int k = 0; k < countrynames.length(); k++) {
                                //JSONObject countryname = countrynames.getJSONObject(k);
                                country = countrynames.optString("displayName");

                            }

                        }
                        City c = new City(country, cityname, id);
                        Log.i("City", c.toString());
                        listener.CityAvailable(c);

                    }

                //}


            }


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
    public interface CityAvailable {
        void CityAvailable(City city);
    }
}
