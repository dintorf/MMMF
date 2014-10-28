package dintorf.mmmf;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dintorf on 10/17/14.
 */
public class LocalLineupFragment extends Fragment {

    ListView lineupListView;
    ListViewAdapter adapter;
    SeparatedListAdapter sladapter;

    private LoadLineup load;

    private ProgressDialog pDialog;

    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> lineupListFriday;
    ArrayList<HashMap<String, String>> lineupListSaturday;
    ArrayList<HashMap<String, String>> lineupListSunday;

    private static String url_get_lineup = "http://mmmf.herokuapp.com/getLineup.php";

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_LINEUP = "lineup";
    protected static final String TAG_NAME = "artist_name";
    protected static final String TAG_BIO_SHORT = "artist_bio_short";
    protected static final String TAG_STAGE = "stage";
    protected static final String TAG_IMAGE = "artist_img";
    protected static final String TAG_PERFORMDATE = "play_time";
    protected static final String TAG_PERFORMTIME = "perform_time";

    JSONArray lineup = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.lineup_view, container, false);

        // setup tabhost for main and local stage
//        TabHost tabHost = (TabHost) rootView.findViewById(R.id.tabHost);
//
//        tabHost.setup();
//
//        this.setNewTab(tabHost,"main",R.string.main,R.id.tabMain);
//        this.setNewTab(tabHost,"local",R.string.local,R.id.tabLocal);

//        TabHost.TabSpec tabSpec = tabHost.newTabSpec("main");
//        tabSpec.setContent(R.id.tabMain);
//        tv.setText("Main Stage");
//        tabSpec.setIndicator(view);
//        tabHost.addTab(tabSpec);
//
//        tabSpec = tabHost.newTabSpec("local");
//        tabSpec.setContent(R.id.tabLocal);
//        tv.setText("Local Stage");
//        tabSpec.setIndicator(view);
//        tabHost.addTab(tabSpec);

        // initialize list views and lineup ArrayLists
        lineupListView = (ListView) rootView.findViewById(R.id.mainLineupListView);

        lineupListFriday = new ArrayList<HashMap<String, String>>();
        lineupListSaturday = new ArrayList<HashMap<String, String>>();
        lineupListSunday = new ArrayList<HashMap<String, String>>();


        // execute loading lineup
        load = new LoadLineup();
        load.execute();

        return rootView;
    }

    private void setNewTab(TabHost tabHost, String tag, int title, int contentID ){
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setIndicator(getTabIndicator(tabHost.getContext(), title)); // new function to inject our own tab layout
        tabSpec.setContent(contentID);
        tabHost.addTab(tabSpec);
    }

    private View getTabIndicator(Context context, int title) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadLineup extends AsyncTask<String, String, String> {

        private String[] dateArr = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        protected JSONObject json;

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading lineup. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("stage","main"));
//            // getting JSON string from URL
//            json = jParser.makeHttpRequest(url_get_lineup, "GET", params);


            ParseQuery<ParseObject> query  = ParseQuery.getQuery("Lineup");
            query.whereEqualTo("stage","local");
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
                Log.e("Lineup",lineupListFriday.get(0).toString());
//                        String name =  parseObjects.toString();
//                        Log.d("THE QUERY ", "" + name);
            }
            catch(ParseException e) {
                Log.e("ERROR:", "" + e.getMessage());
            }
            catch(Exception e2) {
                Log.e("ERROR:", e2.getMessage());
            }



            // Check your log cat for JSON reponse
//            Log.d("Full Lineup: ", json.toString());

//            try {
//                // Checking for SUCCESS TAG
//                int success = json.getInt(TAG_SUCCESS);
//
//                if (success == 1) {
//                    // products found
//                    // Getting Array of lineups
//                    lineup = json.getJSONArray(TAG_LINEUP);
//
//                    // looping through all lineups
//                    for (int i = 0; i < lineup.length(); i++) {
//                        JSONObject c = lineup.getJSONObject(i);
//
//                        // Storing each json item in variable
////                        String artist_name = c.getString(TAG_NAME);
////                        String artist_bio = c.getString(TAG_BIO_SHORT);
////                        String stage_name = c.getString(TAG_STAGE);
////                        String img_str = c.getString(TAG_IMAGE);
////                        String perform_date = c.getString(TAG_PERFORMDATE);
////
////                        // getting date and time
////                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                        Date date = null;
////                        try{
////                            date = format.parse(perform_date);
////                        }
////                        catch(Exception e){
////                            Log.e("MainActivity", "Error parsing date " + e.toString());
////                        }
////
////                        String time = new SimpleDateFormat("hh:mm aaa").format(date);
////                        String longDate = new SimpleDateFormat("EEE MMMM d, yyyy hh:mm aaa").format(date);
////
////
////                        // creating new HashMap
////                        HashMap<String, String> map = new HashMap<String, String>();
////
////                        // adding each child node to HashMap key => value
////                        map.put(TAG_NAME, artist_name);
////                        map.put(TAG_BIO_SHORT, artist_bio);
////                        map.put(TAG_STAGE, stage_name);
////                        map.put(TAG_IMAGE, img_str);
////                        map.put(TAG_PERFORMTIME, time);
////                        map.put(TAG_PERFORMDATE, longDate);
////
////                        // adding HashList to ArrayList
////                        String day = dateArr[date.getDay()];
////                        if(day.equals("Friday")){
////                            lineupListFriday.add(map);
////                        }
////                        else if(day.equals("Saturday")) {
////                            lineupListSaturday.add(map);
////                        }
////                        else if(day.equals("Sunday")) {
////                            lineupListSunday.add(map);
////                        }
//                    }
//                } else {
////                    // no products found
////                    // Launch Add New product Activity
////                    Intent i = new Intent(getApplicationContext(),
////                            NewProductActivity.class);
////                    // Closing all previous activities
////                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                    startActivity(i);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    // Create main stage list
                    sladapter = new SeparatedListAdapter(getActivity());
                    adapter = new ListViewAdapter(getActivity(),lineupListFriday);
                    sladapter.addSection("Friday March 27th",adapter);

                    adapter = new ListViewAdapter(getActivity(),lineupListSaturday);
                    sladapter.addSection("Saturday March 28th",adapter);

                    adapter = new ListViewAdapter(getActivity(),lineupListSunday);
                    sladapter.addSection("Sunday March 29th",adapter);

                    lineupListView.setAdapter(sladapter);

//                    // Create local stage list
//                    sladapter = new SeparatedListAdapter(getActivity());
//                    adapter = new ListViewAdapter(getActivity(),localLineupListFriday);
//                    sladapter.addSection("Friday March 27th",adapter);
//
//                    adapter = new ListViewAdapter(getActivity(),localLineupListSaturday);
//                    sladapter.addSection("Saturday March 28th",adapter);
//
//                    adapter = new ListViewAdapter(getActivity(),localLineupListSunday);
//                    sladapter.addSection("Sunday March 29th",adapter);
//
//                    localLineupListView.setAdapter(sladapter);
                }
            });

        }

    }
}
