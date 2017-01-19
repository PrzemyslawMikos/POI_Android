package additional;

import android.app.Activity;
import android.widget.RelativeLayout;
import com.adventure.poi.poi_android.R;
import java.util.HashMap;

/**
 * Created by Przemek on 19.01.2017.
 */

public class MenuHelper {

    private HashMap<Integer, RelativeLayout> hashMap;

    public MenuHelper(Activity activity){
        hashMap = new HashMap<>();
        hashMap.put(R.id.manu_types_element, (RelativeLayout) activity.findViewById(R.id.manu_types_element));
        hashMap.put(R.id.manu_google_element, (RelativeLayout) activity.findViewById(R.id.manu_google_element));
        hashMap.put(R.id.manu_add_element, (RelativeLayout) activity.findViewById(R.id.manu_add_element));
        hashMap.put(R.id.manu_user_data_element, (RelativeLayout) activity.findViewById(R.id.manu_user_data_element));
        hashMap.put(R.id.manu_logout_element, (RelativeLayout) activity.findViewById(R.id.manu_logout_element));
    }

    public RelativeLayout getById(Integer id){
        return hashMap.get(id);
    }

}