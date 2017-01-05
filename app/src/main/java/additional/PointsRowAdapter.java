package additional;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adventure.poi.poi_android.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import entity.PointEntity;

/**
 * Created by Przemek on 02.01.2017.
 */

public class PointsRowAdapter extends ArrayAdapter<PointEntity> {
    Context context;
    int layoutId;
    ArrayList<PointEntity> data = null;

    public PointsRowAdapter(Context context, int layoutId, ArrayList<PointEntity> data) {
        super(context,layoutId, data);
        this.context = context;
        this.data = data;
        this.layoutId = layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        RowPointEntity holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutId, parent, false);

            holder = new RowPointEntity();
            holder.imgIcon = (TextView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else
        {
            holder = (RowPointEntity) row.getTag();
        }

        PointEntity object = data.get(position);
        holder.txtTitle.setText("Id:" + object.getId().toString());
        holder.imgIcon.setText(object.getDescription());

        return row;
    }

    static class RowPointEntity
    {
        TextView imgIcon;
        TextView txtTitle;
    }

}
