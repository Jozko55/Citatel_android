package com.example.jozko.citatel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Citatel extends Activity {

    ListView list;
    TextView name;
    TextView description;
    Button btnGetData;
    ArrayList<HashMap<String, String>> bookList = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array
    private static String url = "http://citatel.herokuapp.com/books.json";

    //JSON Node Names
    private static final String TAG_NAME = "name";
    private static final String TAG_DESCRIPTION = "description";

    JSONArray books = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_citatel);
        bookList = new ArrayList<HashMap<String, String>>();

        btnGetData = (Button)findViewById(R.id.get_books);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONParse().execute();
            }
        });

    }

    private class JSONParse extends AsyncTask<String, String, JSONArray> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            name = (TextView)findViewById(R.id.name);
            description = (TextView)findViewById(R.id.description);
            pDialog = new ProgressDialog(Citatel.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONArray doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONArray json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
                books = json;
                bookList.clear();
                for(int i = 0; i < books.length(); i++){
                    JSONObject c = books.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String name = c.getString(TAG_NAME);
                    String description = c.getString(TAG_DESCRIPTION);

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_NAME, name);
                    map.put(TAG_DESCRIPTION, description);

                    bookList.add(map);
                    list=(ListView)findViewById(R.id.list);

                    ListAdapter adapter = new SimpleAdapter(Citatel.this, bookList,
                            R.layout.book_detail,
                            new String[] { TAG_NAME, TAG_DESCRIPTION }, new int[] {
                            R.id.name, R.id.description});

                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Toast.makeText(Citatel.this, "You Clicked at " + bookList.get(+position).get("name"), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
