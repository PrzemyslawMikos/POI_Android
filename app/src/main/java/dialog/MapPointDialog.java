package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.adventure.poi.poi_android.PointActivity;
import com.adventure.poi.poi_android.R;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import java.net.MalformedURLException;
import java.net.URL;

import task.ImageManager;
import constants.MainConstants;
import constants.RestConstants;
import delegates.ImageTaskDelegate;
import delegates.RestTaskDelegate;
import entity.PointEntity;
import google.GoogleNavi;
import rest.TypesHelper;

/**
 * Created by Przemysław Mikos on 12.01.2017.
 */

public class MapPointDialog implements MainConstants, RestConstants {

    private Dialog mapDialog;
    private PointRatingDialog pointRatingDialog;
    private ImageButton buttonNaviMap, buttonMapRate, buttonMapDetails;
    private ImageView imageViewPointPicture;
    private Activity activity;
    private PointEntity point;
    private TypesHelper typesHelper;

    public MapPointDialog(Activity activity, PointEntity point){
        this.activity = activity;
        this.point = point;
        pointRatingDialog = new PointRatingDialog(activity, point);
    }

    public void show(){
        mapDialog = new Dialog(activity);
        mapDialog.setContentView(R.layout.point_map_dialog);
        mapDialog.setCancelable(true);

        TextView textViewPointName = (TextView) mapDialog.findViewById(R.id.map_dialog_point_name);
        textViewPointName.setText(point.getName());
        TextView textViewPointRating = (TextView) mapDialog.findViewById(R.id.map_dialog_point_rating);
        textViewPointRating.setText(String.format(activity.getResources().getString(R.string.map_dialog_rating), Double.toString(point.getRating())));

        imageViewPointPicture = (ImageView) mapDialog.findViewById(R.id.map_dialog_point_picture);
        ImageManager imageManager = new ImageManager(new ImageTaskDelegate() {
            @Override
            public void TaskCompletionResult(Bitmap result) {
                imageViewPointPicture.setImageBitmap(result);
                mapDialog.show();
            }
        });
        try{
            URL url = new URL(String.format(REST_POINTS_IMAGE, point.getPicture()));
            imageManager.runTask(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        buttonMapRate = (ImageButton) mapDialog.findViewById(R.id.buttonMapRate);
        buttonMapRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pointRatingDialog.show();
            }
        });

        buttonNaviMap = (ImageButton) mapDialog.findViewById(R.id.buttonNaviMap);
        buttonNaviMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleNavi.startNavi(GOOGLE_NAVI_CAR, activity, point);
            }
        });

        buttonMapDetails = (ImageButton) mapDialog.findViewById(R.id.buttonDetailsMap);
        buttonMapDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typesHelper = new TypesHelper(activity, new RestTaskDelegate() {
                    @Override
                    public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                        Intent intent = new Intent(activity, PointActivity.class);;
                        intent.putExtra(SERIAlIZABLE_POINT, point);
                        intent.putExtra(SERIAlIZABLE_TYPE, typesHelper.getTypes().get(0));
                        activity.startActivity(intent);
                    }
                });
                typesHelper.getTypeById(activity.getResources().getString(R.string.map_downloading_data), point.getTypeid());
            }
        });
    }
}