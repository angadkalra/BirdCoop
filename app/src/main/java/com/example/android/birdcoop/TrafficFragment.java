package com.example.android.birdcoop;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;



/**
 * A placeholder fragment containing a simple view.
 */
public class TrafficFragment extends Fragment {

    private final static String token = "9b3c752574644c2c209f8d5033da073d22b13cb8";
    private View rootView;

    public TrafficFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FetchTrafficData fetchTrafficData = new FetchTrafficData();
        fetchTrafficData.execute();

        rootView = inflater.inflate(R.layout.fragment_traffic, container, false);

        return rootView;
    }

    public class FetchTrafficData extends AsyncTask<Void, Void, Map<String, Integer>> {

        private Map<String, Integer> trafficData;

        private String currentTime;

        private void getTime() {
            DateTimeZone date = DateTimeZone.forOffsetHours(-7);
            DateTime dt = new DateTime(date);
            if (dt.getHourOfDay() == 12) {
                currentTime = dt.getHourOfDay() + ":" + dt.getMinuteOfHour() + " pm";
                return;
            }
            if (dt.getHourOfDay() > 12) {
                if (dt.getMinuteOfHour() > 9) {
                    currentTime = dt.getHourOfDay() - 12 + ":" + dt.getMinuteOfHour() + " pm";
                    return;
                }
                currentTime = dt.getHourOfDay() - 12 + ":0" + dt.getMinuteOfHour() + " pm";
                return;
            }
            if (dt.getMinuteOfHour() > 9) {
                currentTime = dt.getHourOfDay() + ":" + dt.getMinuteOfHour() + " am";
                return;
            }
            currentTime = dt.getHourOfDay() + ":0" + dt.getMinuteOfHour() + " am";


        }

        private Map<String, Integer> getDataFromJson(String trafficJsonStr)
                throws JSONException {

            trafficData = new HashMap<String, Integer>();

            // These are the values of the JSON objects that need to be extracted.
            int capacity;
            JSONObject current_count;
            int count;
            int currently_open;
            String time;

            JSONObject trafficJson = new JSONObject(trafficJsonStr);

            capacity = trafficJson.getInt("capacity");
            current_count = trafficJson.getJSONObject("current_count");
            count = current_count.getInt("count");
            currently_open = (trafficJson.getBoolean("currently_open") == true) ? 1 : 0;

            trafficData.put("currently_open", currently_open);
            trafficData.put("capacity", capacity);
            trafficData.put("current_count", count);

            return trafficData;
        }

        @Override
        protected Map<String, Integer> doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String trafficJsonStr = null;

            try {
                // Construct the URL
                URL url = new URL("https://sandbox.density.io/v1/locations/1/");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Token " + token);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                trafficJsonStr = buffer.toString();

                Log.d("FetchTrafficData", trafficJsonStr);


            } catch (IOException e) {
                Log.e("FetchTrafficData", "Error ", e);
                // If the code didn't successfully get the data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("FetchTrafficData", "Error closing stream", e);
                    }
                }
            }

            try {
                return getDataFromJson(trafficJsonStr);
            }
            catch(JSONException e) {
                Log.e("FetchTrafficData", e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Map<String, Integer> results) {
            super.onPostExecute(results);

            // Need to update the UI with the information from JSON query. First,
            // set up UI with proper widgets and layouts.

            // BirdCoop capacity is 300. 0-25 is low traffic. 25-75 is steady traffic.
            // 75-125 is High Traffic. 125+ is near capacity.

            // Need to look at current_count and update progress bar and the text view.
            if (!results.isEmpty()) {
                ProgressBar progress_circle = (ProgressBar) rootView.findViewById(R.id.progressBar);
                TextView progressState = (TextView) rootView.findViewById(R.id.progressTextView);
                TextView progressUpdateTime = (TextView) rootView.findViewById(R.id.progressUpdateTime);

                getTime();

                int count = results.get("current_count");

                if (count < 25) {
                    progressState.setText("Low Traffic");
                    progress_circle.setProgress(25);
                }

                else if(count >= 25 && count < 75) {
                    progressState.setText("Steady Traffic");
                    progress_circle.setProgress(50);

                }
                else if(count >= 75 && count <= 125) {
                    progressState.setText("High Traffic");
                    progress_circle.setProgress(75);
                }
                else if(count > 125) {
                    progressState.setText("Near Capacity");
                    progress_circle.setProgress(90);
                }

                progressUpdateTime.setText("Updated @ " + currentTime);
            }
        }
    }
}
