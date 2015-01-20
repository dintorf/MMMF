package dintorf.mmmf;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by dintorf on 10/17/14.
 */

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageDownloader imageLoader;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public final Map<String,Adapter> sections = new LinkedHashMap<String,Adapter>();
    public final ArrayAdapter<String> headers;

    public ListViewAdapter(Context context,
                           ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        imageLoader = new ImageDownloader();
        headers = new ArrayAdapter<String>(context, R.layout.list_item);
    }

    public void addSection(String section, Adapter adapter) {
        this.headers.add(section);
        this.sections.put(section, adapter);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView name;
        TextView bio;
//        TextView stage;
        TextView time;
        ImageView img;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        name = (TextView) itemView.findViewById(R.id.artname);
        bio = (TextView) itemView.findViewById(R.id.artbio);
//        stage = (TextView) itemView.findViewById(R.id.stagename);
        time = (TextView) itemView.findViewById(R.id.perftime);

        // Locate the ImageView in listview_item.xml
        img = (ImageView) itemView.findViewById(R.id.artimg);
            // Capture position and set results to the TextViews
            name.setText(resultp.get(LineupActivity.TAG_NAME));
            bio.setText(resultp.get(LineupActivity.TAG_BIO_SHORT));
//            stage.setText(resultp.get(LineupActivity.TAG_STAGE));
            time.setText(resultp.get(LineupActivity.TAG_PERFORMTIME));
            // Capture position and set results to the ImageView
            Glide.with(context).load(resultp.get(LineupActivity.TAG_IMAGE)).into(img);
            // Capture ListView item click
            itemView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);
                Intent intent = new Intent(context, SingleItemView.class);
                // Pass all data name
                intent.putExtra("name", resultp.get(LineupActivity.TAG_NAME));
                // Pass all data bio
                intent.putExtra("bio", resultp.get(LineupActivity.TAG_BIO_SHORT));
                // Pass all data stage
//                intent.putExtra("stage",resultp.get(LineupActivity.TAG_STAGE));
                // Pass all data date
                intent.putExtra("date",resultp.get(LineupActivity.TAG_PERFORMDATE));
                // Pass all data img
                intent.putExtra("img", resultp.get(LineupActivity.TAG_IMAGE));
                // Start SingleItemView Class
                context.startActivity(intent);

                }
            });

        return itemView;
    }
}