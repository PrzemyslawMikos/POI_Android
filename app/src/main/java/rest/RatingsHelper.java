package rest;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import com.adventure.poi.poi_android.R;
import org.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import additional.SnackbarManager;
import delegates.RestTaskDelegate;
import entity.RatingEntity;
import entity.StatusEntity;

/**
 * Created by Przemysław Mikos on 12.01.2017.
 */

public class RatingsHelper extends EntityHelper{

    public RatingsHelper(Activity activity, RestTaskDelegate delegate) {
        super(activity, delegate);
    }

    public void postRating(String message, RatingEntity ratingEntity){
        HttpHeaders header = getHeaderWithBearer();
        restHelper = new RestHelper(REST_RATINGS_POST, HttpMethod.POST, header, ratingEntity.toJSON(), super.getActivity(), message, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                if (restHelper.getResponseEntity().getStatusCode() == HttpStatus.OK) {
                    showMessages(new StatusEntity(restHelper.getResponseEntity().getBody()));
                    RatingsHelper.super.getDelegate().TaskCompletionResult(restHelper.getResponseEntity());
                }
                else{
                    showMessages(restHelper.getStatus());
                }
            }
        });
        restHelper.runTask();
    }

    private void showMessages(StatusEntity entity) {
        switch (entity.getStatus()){
            case STATUS_NEW:
                SnackbarManager.showSnackbar(activity, activity.getResources().getString(R.string.rating_new), Snackbar.LENGTH_LONG);
                break;
            case STATUS_UPDATED:
                SnackbarManager.showSnackbar(activity, activity.getResources().getString(R.string.rating_updated), Snackbar.LENGTH_LONG);
                break;
            case STATUS_INTERNAL_SERVER_ERROR:
                SnackbarManager.showSnackbar(activity, activity.getResources().getString(R.string.server_exception), Snackbar.LENGTH_LONG);
                break;
        }
    }
}