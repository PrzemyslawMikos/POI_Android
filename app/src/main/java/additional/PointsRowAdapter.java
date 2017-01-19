package additional;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.adventure.poi.poi_android.R;
import java.net.URL;
import java.util.ArrayList;
import constants.RestConstants;
import entity.PointEntity;

/**
 * Created by Przemysław Mikos on 02.01.2017.
 */

public class PointsRowAdapter extends ArrayAdapter<PointEntity> implements RestConstants{

    private LayoutInflater layoutInflater;
    private ArrayList<PointEntity> data = null;

    public PointsRowAdapter(Context context, int layoutId, ArrayList<PointEntity> data) {
        super(context, layoutId, data);
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void addToList(ArrayList<PointEntity> listPoints){
        for (PointEntity point: listPoints) {
            data.add(point);
        }
    }

    public ArrayList<PointEntity> getList(){
        return data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        PointsRowAdapter.RowPointEntity holder;
        PointEntity object = data.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_list_points, null);
            holder = new PointsRowAdapter.RowPointEntity();
            holder.imageViewImage = (ImageView) convertView.findViewById(R.id.imageViewPointPicture);
            holder.textViewName = (TextView) convertView.findViewById(R.id.textViewPointName);
            holder.textViewDescription = (TextView) convertView.findViewById(R.id.textViewPointDescription);
            holder.textViewDate = (TextView) convertView.findViewById(R.id.textViewPointDate);
            holder.textViewRating = (TextView) convertView.findViewById(R.id.textViewPointRating);
            convertView.setTag(holder);
        } else {
            holder = (PointsRowAdapter.RowPointEntity) convertView.getTag();
        }
        holder.textViewName.setText(object.getName());
        holder.textViewDescription.setText(object.getDescription());
        holder.textViewDate.setText(object.getAddeddate());
        holder.textViewRating.setText(object.getRating().toString());
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
        TextView textViewName, textViewDescription, textViewDate, textViewRating;
    }
}