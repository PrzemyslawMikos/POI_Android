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
import entity.TypeEntity;

/**
 * Created by Przemek on 08.01.2017.
 */

public class TypesRowAdapter extends ArrayAdapter<TypeEntity> implements RestConstants{

    Context context;
    int layoutId;
    private LayoutInflater layoutInflater;
    ArrayList<TypeEntity> data = null;

    public TypesRowAdapter(Context context, int layoutId, ArrayList<TypeEntity> data) {
        super(context,layoutId, data);
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        this.layoutId = layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        RowTypeEntity holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_list_types, null);
            holder = new RowTypeEntity();
            holder.imageViewImage = (ImageView) convertView.findViewById(R.id.imageViewTypePicture);
            holder.textViewName = (TextView) convertView.findViewById(R.id.textViewTypeName);
            holder.textViewDescription = (TextView) convertView.findViewById(R.id.textViewTypeDescription);
            holder.textViewDate = (TextView) convertView.findViewById(R.id.textViewTypeDate);
            convertView.setTag(holder);
        } else {
            holder = (RowTypeEntity) convertView.getTag();
        }

        TypeEntity object = data.get(position);
        holder.textViewName.setText(object.getName());
        holder.textViewDescription.setText(object.getDescription());
        holder.textViewDate.setText(object.getAddeddate());
        if (holder.imageViewImage != null) {
            try{
                new PictureLoader(holder.imageViewImage).execute(new URL(String.format(REST_TYPES_IMAGE, object.getImage())));
            }catch (Exception e){

            }
        }
        return convertView;
    }

    static class RowTypeEntity
    {
        ImageView imageViewImage;
        TextView textViewName, textViewDescription, textViewDate;
    }

}