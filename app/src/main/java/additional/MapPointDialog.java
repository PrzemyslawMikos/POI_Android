package additional;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.adventure.poi.poi_android.R;

import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;

import constants.MainConstants;
import constants.RestConstants;
import delegates.ImageTaskDelegate;
import delegates.RestTaskDelegate;
import entity.PointEntity;
import entity.RatingEntity;
import google.GoogleNavi;
import rest.RatingsHelper;

import static constants.RestConstants.REST_POINTS_IMAGE;

/**
 * Created by Przemek on 12.01.2017.
 */

public class MapPointDialog implements MainConstants, RestConstants {

    private Dialog mapDialog;
    private PointRatingDialog pointRatingDialog;
    private ImageButton buttonNaviMap, buttonMapRate, buttonMapDetails;
    private ImageView imageViewPointPicture;
    private Activity activity;
    private PointEntity point;
    private RatingsHelper ratingsHelper;

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
                // TODO wywołanie aktywności point_activity pobranie typu i pokazanie danych punktu
            }
        });
    }
}