package rest;

import android.app.Activity;
import android.widget.Toast;

import com.adventure.poi.poi_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;

import additional.ToastManager;
import delegates.RestTaskDelegate;
import entity.StatusEntity;
import entity.TypeEntity;

/**
 * Created by Przemek on 13.12.2016.
 */

public class TypesHelper extends EntityHelper{

    private ArrayList<TypeEntity> types;

    public TypesHelper(Activity activity, RestTaskDelegate delegate) {
        super(activity, delegate);
        this.types = new ArrayList<>();
    }

    public ArrayList<TypeEntity> getTypes() {
        return types;
    }

    public List<String> getTypesNamesList(){
        List<String> listTypesNames = new ArrayList<String>();
        for(TypeEntity type:types ){
            listTypesNames.add(type.getName());
        }
        return listTypesNames;
    }

    public void getAllTypes(String message){
        HttpHeaders header = super.getHeaderWithBearer();
        restHelper = new RestHelper(REST_TYPES_GET, HttpMethod.GET, header, super.getActivity(), message, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                if (restHelper.getResponseEntity().getStatusCode() == HttpStatus.OK) {
                    String stypesarray = restHelper.getResponseEntity().getBody();
                    JSONArray jtypesarray = new JSONArray(stypesarray);
                    for (int i = 0; i < jtypesarray.length(); i++) {
                        JSONObject jtype = jtypesarray.getJSONObject(i);
                        TypeEntity type = new TypeEntity(jtype);
                        types.add(type);
                    }
                    TypesHelper.super.getDelegate().TaskCompletionResult(restHelper.getResponseEntity());
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
            case STATUS_NOT_FOUND:
                ToastManager.showToast(activity.getApplicationContext(), activity.getResources().getString(R.string.types_not_found), Toast.LENGTH_LONG);
                break;
            case STATUS_INTERNAL_SERVER_ERROR:
                ToastManager.showToast(activity.getApplicationContext(), activity.getResources().getString(R.string.server_exception), Toast.LENGTH_LONG);
                break;
        }
    }
}