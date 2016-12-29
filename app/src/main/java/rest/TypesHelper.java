package rest;

import android.app.Activity;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;

import additional.RestTaskDelegate;
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
                if(restHelper.getResult()) {
                    if (restHelper.getResponseEntity().getStatusCode() == HttpStatus.OK) {
                        String stypesarray = restHelper.getResponseEntity().getBody();
                        JSONArray jtypesarray = new JSONArray(stypesarray);
                        for (int i = 0; i < jtypesarray.length(); i++) {
                            JSONObject jtype = jtypesarray.getJSONObject(i);
                            TypeEntity type = new TypeEntity(jtype);
                            types.add(type);
                        }
                    }
                }
                else{
                    Toast.makeText(TypesHelper.super.getActivity(), "Wyjatek: " + restHelper.getException().getStatusCode().toString(), Toast.LENGTH_LONG).show();
                }
                TypesHelper.super.getDelegate().TaskCompletionResult(restHelper.getResponseEntity());
            }
        });
        restHelper.runTask();
    }

}