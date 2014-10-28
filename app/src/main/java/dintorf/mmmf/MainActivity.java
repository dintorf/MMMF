package dintorf.mmmf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.parse.Parse;
import com.parse.ParseInstallation;


public class MainActivity extends Activity {
    GridView grid;
    String[] activities = {
            "mainstage",
            "localstage",
            "food",
            "drink",
            "shop",
            "deals"
    } ;
    int[] imageId = {
            R.drawable.mainstagehome,
            R.drawable.localstagehome,
            R.drawable.foodhome,
            R.drawable.drinkshome,
            R.drawable.shoppinghome,
            R.drawable.dealshome
    };
    // Tab titles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "T2Pi3dg49Na7727OvCMjl8aETNiBLSjY20W3eKqJ", "ODvALDTEkqKdBtElcuihd1oE8v48ygK0jJHRbC7d");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        CustomGridHome adapter = new CustomGridHome(MainActivity.this, imageId);
        grid = (GridView)findViewById(R.id.homeGrid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i;
                if(activities[position].equals("mainstage")){
                    i = new Intent(MainActivity.this, LineupActivity.class);
                    i.putExtra("stage","main");
                    startActivity(i);
                }
                else if(activities[position].equals("localstage")){
                    i = new Intent(MainActivity.this, LineupActivity.class);
                    i.putExtra("stage","local");
                    startActivity(i);
                }
                else if(activities[position].equals("food")){
                    i = new Intent(MainActivity.this, FoodActivity.class);
                    startActivity(i);
                }
                else if(activities[position].equals("drink")){
                    i = new Intent(MainActivity.this, DrinksActivity.class);
                    startActivity(i);
                }
                else if(activities[position].equals("shop")){
                    i = new Intent(MainActivity.this, ShopActivity.class);
                    startActivity(i);
                }
                else if(activities[position].equals("deals")){
                    i = new Intent(MainActivity.this, DealsActivity.class);
                    startActivity(i);
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
