package dintorf.mmmf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dintorf on 10/28/14.
 */
public class CustomGridList extends BaseAdapter{

    private Context mContext;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();


    public CustomGridList(Context c, ArrayList<HashMap<String, String>> data) {
        mContext = c;
        this.data = data;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        resultp = data.get(position);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.list_grid_single, null);
            ImageView imageView = (ImageView)grid.findViewById(R.id.list_grid_img);
            Glide.with(mContext).load(resultp.get(FoodActivity.TAG_IMAGE)).into(imageView);

        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}
