package additional;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
//    private ArrayList<TypeEntity> typeEntityArrayList;
//    private LayoutInflater layoutInflater;
//
//    public TypesRowAdapter(Context context, ArrayList<TypeEntity> typeEntityArrayList){
//        this.typeEntityArrayList = typeEntityArrayList;
//        layoutInflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        return typeEntityArrayList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return typeEntityArrayList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        RowTypeEntity holder;
//        if(convertView == null){
//            convertView = layoutInflater.inflate(R.layout.row_list_types, null);
//            holder = new RowTypeEntity();
//            holder.imageViewImage = (ImageView) convertView.findViewById(R.id.imageViewTypePicture);
//            holder.textViewName = (TextView) convertView.findViewById(R.id.textViewTypeName);
//            holder.textViewDescription = (TextView) convertView.findViewById(R.id.textViewTypeDescription);
//            holder.textViewDate = (TextView) convertView.findViewById(R.id.textViewTypeDate);
//            convertView.setTag(holder);
//        }else{
//            holder = (RowTypeEntity) convertView.getTag();
//        }
//        Log.d("Position", Integer.toString(position));
//        TypeEntity item = typeEntityArrayList.get(position);
//        holder.textViewName.setText(item.getName());
//        holder.textViewDescription.setText(item.getDescription());
//        holder.textViewDate.setText(item.getAddeddate());
//        if (holder.imageViewImage != null) {
//            try{
//                new ImageDownloaderTask(holder.imageViewImage).execute(String.format(REST_TYPES_IMAGE, item.getImage()));
//                //new PictureLoader(holder.imageViewImage).execute(new URL(String.format(REST_TYPES_IMAGE, item.getImage())));
//            }catch (Exception ignored){}
//        }
//        return convertView;
//    }
//
//    static class RowTypeEntity {
//        ImageView imageViewImage;
//        TextView textViewName, textViewDescription, textViewDate;
//    }

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
        TypeEntity object = data.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_list_types, null);
            holder = new RowTypeEntity();
            holder.imageViewImage = (ImageView) convertView.findViewById(R.id.imageViewTypePicture);
            holder.textViewName = (TextView) convertView.findViewById(R.id.textViewTypeName);
            holder.textViewDescription = (TextView) convertView.findViewById(R.id.textViewTypeDescription);
            holder.textViewDate = (TextView) convertView.findViewById(R.id.textViewTypeDate);
            if (holder.imageViewImage != null) {
                try{
                    Log.d("position", Integer.toString(position));
                    new PictureLoader(holder.imageViewImage).execute(new URL(String.format(REST_TYPES_IMAGE, object.getImage())));
                }catch (Exception e){

                }
            }
            convertView.setTag(holder);
        } else {
            holder = (RowTypeEntity) convertView.getTag();
        }
        holder.textViewName.setText(object.getName());
        holder.textViewDescription.setText(object.getDescription());
        holder.textViewDate.setText(object.getAddeddate());
        if (holder.imageViewImage != null) {
            try{
                Log.d("position", Integer.toString(position));
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