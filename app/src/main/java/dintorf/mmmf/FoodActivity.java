package dintorf.mmmf;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dintorf on 10/17/14.
 */
public class FoodActivity extends Activity {

    GridView grid;
    CustomGridList adapter;
    String[] weburls;
    String[] imgurls;
    Utils utils;

    ArrayList<HashMap<String, String>> vendorList;

    private ProgressDialog pDialog;

    private LoadFoodVendors load;

    protected static final String TAG_NAME = "vendor_name";
    protected static final String TAG_URL = "vendor_url";
    protected static final String TAG_IMAGE = "vendor_img";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_view);

        utils = new Utils(this);

        ActionBar ab = getActionBar();
        ab.setTitle("Food Vendors");

        vendorList = new ArrayList<HashMap<String, String>>();

        grid = (GridView)findViewById(R.id.listGrid);

        // execute loading vendors
        load = new LoadFoodVendors();
        load.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadFoodVendors extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FoodActivity.this);
            pDialog.setMessage("Loading food vendors. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            if(utils.isNetworkAvailable()){
                ParseQuery<ParseObject> query  = ParseQuery.getQuery("Food");
                query.orderByAscending("vendor_name");

                try {
                    List<ParseObject> parseObjects = query.find();

                    Log.d("THE OBJECT", "" + parseObjects.size());

                    for(ParseObject item : parseObjects){
                        Log.d("THE QUERY ", "" + item.getString("vendor_name"));

                        String vendor_name = item.getString(TAG_NAME);
                        String vendor_url = item.getString(TAG_URL);
                        ParseFile vendor_img = (ParseFile) item.get(TAG_IMAGE);


                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_NAME, vendor_name);
                        map.put(TAG_URL, vendor_url);
                        map.put(TAG_IMAGE, vendor_img.getUrl());

                        vendorList.add(map);
                    }
                }
                catch(ParseException e) {
                    Log.e("ERROR:", "" + e.getMessage());
                }
                catch(Exception e2) {
                    Log.e("ERROR:", e2.getMessage());
                }
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if(!utils.isNetworkAvailable()){
                Toast.makeText(getApplicationContext(), "Please connect to a network and retry!", Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                // updating UI from Background Thread
                FoodActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        /**
                         * Updating parsed JSON data into ListView
                         * */

                        // Create main stage list
                        adapter = new CustomGridList(FoodActivity.this, vendorList);
                        grid.setAdapter(adapter);

                        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                Intent i = new Intent(FoodActivity.this, WebActivity.class);
                                i.putExtra("url", vendorList.get(position).get(TAG_URL));
                                startActivity(i);
                            }
                        });
                    }
                });
            }

        }

    }
}
