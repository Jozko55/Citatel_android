package com.example.jozko.citatel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by Jozko on 15.7.2015.
 */
public class JSONParser {
    static InputStream is = null;
    static JSONArray jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    public JSONArray getJSONFromUrl(String a_url) {

        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(a_url);
            urlConnection = (HttpURLConnection) url.openConnection();
            is = urlConnection.getInputStream();
            return readStream(is);
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            urlConnection.disconnect();
        }
        return null;
    }

    private JSONArray readStream(InputStream is) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            json = sb.toString();
           // is.close();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }
}
