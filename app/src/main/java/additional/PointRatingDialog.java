package additional;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import com.adventure.poi.poi_android.R;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import constants.MainConstants;
import delegates.RestTaskDelegate;
import entity.PointEntity;
import entity.RatingEntity;
import rest.RatingsHelper;

/**
 * Created by Przemek on 12.01.2017.
 */

public class PointRatingDialog implements MainConstants {

    private Dialog ratingDialog;
    private RatingBar ratingBar;
    private Button buttonSaveRating;
    private Activity activity;
    private PointEntity point;
    private RatingsHelper ratingsHelper;

    public PointRatingDialog(Activity activity, PointEntity point){
        this.activity = activity;
        this.point = point;
    }

    public void show(){
        ratingDialog = new Dialog(activity);
        ratingDialog.setContentView(R.layout.rating_dialog);
        ratingDialog.setCancelable(true);
        ratingBar = (RatingBar)ratingDialog.findViewById(R.id.dialog_ratingbar);

        TextView textViewPointName = (TextView) ratingDialog.findViewById(R.id.rating_point_name);
        textViewPointName.setText(point.getName());

        buttonSaveRating = (Button) ratingDialog.findViewById(R.id.buttonSaveRating);
        buttonSaveRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingsHelper = new RatingsHelper(activity, new RestTaskDelegate() {
                    @Override
                    public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {

                    }
                });
                SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(activity.getApplicationContext());
                RatingEntity ratingEntity = new RatingEntity(Math.round(ratingBar.getRating()), point.getId(), Long.parseLong(sharedPreferencesManager.getPreferenceString(PREFERENCE_USERID)));
                ratingsHelper.postRating(activity.getResources().getString(R.string.rating_adding), ratingEntity);
                ratingDialog.dismiss();
            }
        });
        ratingDialog.show();
    }
}