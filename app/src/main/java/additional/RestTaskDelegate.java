package additional;

import org.json.JSONException;
import org.springframework.http.ResponseEntity;

/**
 * Created by Przemek on 04.12.2016.
 */

public interface RestTaskDelegate {
        void TaskCompletionResult(ResponseEntity<String> result) throws JSONException;
}
