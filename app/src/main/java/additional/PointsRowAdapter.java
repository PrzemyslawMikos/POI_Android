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
import java.net.URL;
import java.util.ArrayList;

import entity.PointEntity;
import entity.TypeEntity;

import static constants.RestConstants.REST_POINTS_IMAGE;
import static constants.RestConstants.REST_TYPES_IMAGE;

/**
 * Created by Przemek on 02.01.2017.
 */

public class PointsRowAdapter extends ArrayAdapter<PointEntity> {

    Context context;
    int layoutId;
    private LayoutInflater layoutInflater;
    ArrayList<PointEntity> data = null;

    public PointsRowAdapter(Context context, int layoutId, ArrayList<PointEntity> data) {
        super(context,layoutId, data);
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        this.layoutId = layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        TypesRowAdapter.RowTypeEntity holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_list_points, null);
            holder = new TypesRowAdapter.RowTypeEntity();
            holder.imageViewImage = (ImageView) convertView.findViewById(R.id.imageViewPointPicture);
            holder.textViewName = (TextView) convertView.findViewById(R.id.textViewPointName);
            holder.textViewDescription = (TextView) convertView.findViewById(R.id.textViewPointDescription);
            holder.textViewDate = (TextView) convertView.findViewById(R.id.textViewPointDate);
            convertView.setTag(holder);
        } else {
            holder = (TypesRowAdapter.RowTypeEntity) convertView.getTag();
        }

        PointEntity object = data.get(position);
        holder.textViewName.setText(object.getName());
        holder.textViewDescription.setText(object.getDescription());
        holder.textViewDate.setText(object.getAddeddate());
        if (holder.imageViewImage != null) {
            try{
                new PictureLoader(holder.imageViewImage).execute(new URL(String.format(REST_POINTS_IMAGE, object.getPicture())));
            }catch (Exception e){

            }
        }
        return convertView;
    }

    static class RowPointEntity
    {
        ImageView imageViewImage;
        TextView textViewName, textViewDescription, textViewDate;
    }

}