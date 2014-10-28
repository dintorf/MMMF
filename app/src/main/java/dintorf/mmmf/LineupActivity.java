package dintorf.mmmf;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dintorf on 10/17/14.
 */
public class LineupActivity extends Activity {

    ListView lineupListView;
    ListViewAdapter adapter;
    SeparatedListAdapter sladapter;

    String stage;

    private LoadLineup load;

    private ProgressDialog pDialog;

    ArrayList<HashMap<String, String>> lineupListFriday;
    ArrayList<HashMap<String, String>> lineupListSaturday;
    ArrayList<HashMap<String, String>> lineupListSunday;

    protected static final String TAG_NAME = "artist_name";
    protected static final String TAG_BIO_SHORT = "artist_bio_short";
    protected static final String TAG_STAGE = "stage";
    protected static final String TAG_IMAGE = "artist_img";
    protected static final String TAG_PERFORMDATE = "play_time";
    protected static final String TAG_PERFORMTIME = "perform_time";


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lineup_view);

        Intent i = getIntent();
        stage = i.getExtras().getString("stage");

        String title = (stage.equals("main")? "Main Stage Lineup" : "Local Stage Lineup");;

        ActionBar ab = getActionBar();
        ab.setTitle(title);
        ab.setDisplayHomeAsUpEnabled(true);

        // initialize list views and lineup ArrayLists
        lineupListView = (ListView) findViewById(R.id.mainLineupListView);

        lineupListFriday = new ArrayList<HashMap<String, String>>();
        lineupListSaturday = new ArrayList<HashMap<String, String>>();
        lineupListSunday = new ArrayList<HashMap<String, String>>();


        // execute loading lineup
        load = new LoadLineup();
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
    class LoadLineup extends AsyncTask<String, String, String> {

        private String[] dateArr = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LineupActivity.this);
            pDialog.setMessage("Loading lineup. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            ParseQuery<ParseObject> query  = ParseQuery.getQuery("Lineup");
            query.whereEqualTo("stage", stage);
            query.orderByAscending("play_time");

            try {
                List<ParseObject> parseObjects = query.find();

                Log.d("THE OBJECT", "" +parseObjects.size());

                for(ParseObject item : parseObjects){
                    Log.d("THE QUERY ", "" + item.getString("artist_name"));

                    String artist_name = item.getString(TAG_NAME);
                    String artist_bio = item.getString(TAG_BIO_SHORT);
                    String stage_name = item.getString(TAG_STAGE);
//                            String img_str = item.getString(TAG_IMAGE);
                    Date perform_date = item.getDate(TAG_PERFORMDATE);
                    ParseFile image = (ParseFile) item.get("artist_img");

                    String time = new SimpleDateFormat("hh:mm aaa").format(perform_date);
                    String longDate = new SimpleDateFormat("EEE MMMM d, yyyy hh:mm aaa").format(perform_date);


                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_NAME, artist_name);
                    map.put(TAG_BIO_SHORT, artist_bio);
                    map.put(TAG_STAGE, stage_name);
//                            map.put(TAG_IMAGE, img_str);
                    map.put(TAG_PERFORMTIME, time);
                    map.put(TAG_PERFORMDATE, longDate);
                    map.put(TAG_IMAGE, image.getUrl());

                    // adding HashList to ArrayList
                    String day = dateArr[perform_date.getDay()];
                    if(day.equals("Friday")){
                        lineupListFriday.add(map);
                    }
                    else if(day.equals("Saturday")) {
                        lineupListSaturday.add(map);
                    }
                    else if(day.equals("Sunday")) {
                        lineupListSunday.add(map);
                    }
                }
            }
            catch(ParseException e) {
                Log.e("ERROR:", "" + e.getMessage());
            }
            catch(Exception e2) {
                Log.e("ERROR:", e2.getMessage());
            }


            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            LineupActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    // Create main stage list
                    sladapter = new SeparatedListAdapter(LineupActivity.this);
                    adapter = new ListViewAdapter(LineupActivity.this, lineupListFriday);
                    sladapter.addSection("Friday March 27th", adapter);

                    adapter = new ListViewAdapter(LineupActivity.this, lineupListSaturday);
                    sladapter.addSection("Saturday March 28th", adapter);

                    adapter = new ListViewAdapter(LineupActivity.this, lineupListSunday);
                    sladapter.addSection("Sunday March 29th", adapter);

                    lineupListView.setAdapter(sladapter);
                }
            });

        }

    }
}
