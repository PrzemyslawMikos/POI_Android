package delegates;

import additional.GeoAddress;

/**
 * Created by Przemek on 04.01.2017.
 */

public interface GoogleLocationTaskDelegate {
    void TaskCompletionResult(GeoAddress result);
}
