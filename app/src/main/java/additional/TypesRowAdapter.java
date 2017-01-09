package additional;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adventure.poi.poi_android.NavigationActivity;
import com.adventure.poi.poi_android.R;

import java.net.URL;
import java.util.ArrayList;

import delegates.ImageTaskDelegate;
import entity.TypeEntity;

import static constants.RestConstants.REST_TYPES_IMAGE;

/**
 * Created by Przemek on 08.01.2017.
 */

public class TypesRowAdapter extends ArrayAdapter<TypeEntity> {

    Context context;
    int layoutId;
    ArrayList<TypeEntity> data = null;

    public TypesRowAdapter(Context context, int layoutId, ArrayList<TypeEntity> data) {
        super(context,layoutId, data);
        this.context = context;
        this.data = data;
        this.layoutId = layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        TypesRowAdapter.RowTypeEntity holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutId, parent, false);

            holder = new TypesRowAdapter.RowTypeEntity();
            holder.imgPhoto = (ImageView) row.findViewById(R.id.imgPhoto);
            holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);
            row.setTag(holder);
        }
        else
        {
            holder = (TypesRowAdapter.RowTypeEntity) row.getTag();
        }



        TypeEntity object = data.get(position);
        holder.txtTitle.setText(object.getName());
        PictureLoader pictureLoader = new PictureLoader(holder.imgPhoto);
        try{
            pictureLoader.execute(new URL(String.format(REST_TYPES_IMAGE, object.getImage())));
        }
        catch (Exception e){

        }

        return row;
    }

    static class RowTypeEntity
    {
        ImageView imgPhoto;
        TextView txtTitle;
    }

}