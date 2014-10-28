package dintorf.mmmf;

/**
 * Created by dintorf on 10/23/14.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class SingleItemView extends Activity {
    // Declare Variables
    String name;
    String bio;
    String stage;
    String date;
    String time;
    String img;
    ImageDownloader imageDownloader;
    String position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.singleitemview);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView calImg = (ImageView) findViewById(R.id.calIcon);
        calImg.setImageResource(R.drawable.caltimeicon);

        imageDownloader = new ImageDownloader();

        Intent i = getIntent();
        // Get the result of name
        name = i.getStringExtra("name");
        // Get the result of bio
        bio = i.getStringExtra("bio");
        // Get the result of stage
        stage = i.getStringExtra("stage");
        // Get the result of date
        date = i.getStringExtra("date");
        // Get the result of time
        time = i.getStringExtra("time");
        // Get the result of img
        img = i.getStringExtra("img");

        // Locate the TextViews in singleitemview.xml
//        TextView txtName = (TextView) findViewById(R.id.name);
//        TextView txtBio = (TextView) findViewById(R.id.bio);
//        TextView txtStage = (TextView) findViewById(R.id.stage);
        TextView txtDate = (TextView) findViewById(R.id.date);

        // Locate the ImageView in singleitemview.xml
        ImageView imgView = (ImageView) findViewById(R.id.img);

        // Set results to the TextViews
//        txtName.setText(name);
//        txtBio.setText(bio);
//        txtStage.setText(stage);]

        txtDate.setText(date);

        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
//        imageDownloader.download(img, imgView);
        Glide.with(this).load(img).into(imgView);

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
}