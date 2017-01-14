package constants;

/**
 * Created by Przemek on 12.12.2016.
 */

public interface MainConstants {

    // Współdzielone preferencje
    String PREFERENCES_NAME = "PoiAndroid";
    String PREFERENCE_USERNAME = "username";
    String PREFERENCE_USERID = "userid";
    String PREFERENCE_PASSWORD = "password";
    String PREFERENCE_TOKEN = "token";
    String PREFERENCE_REMEMBER_ME = "remember_me";

    // Uprawnienia
    int PERMISSION = 1;

    // Nazwa tymczasowego zdjęcia
    String IMAGE_TMP_NAME = "TmpImgPoi.jpeg";

    // Rozmiar zdjęć
    int IMAGE_RESIZED_HEIGHT = 600;
    int IMAGE_RESIZED_WIDTH = 800;

    // Przekazywanie pomiędzy aktywnościami
    String SERIAlIZABLE_POINT = "point";
    String SERIAlIZABLE_TYPE= "type";

    // Formaty zapytań do Google maps
    String GOOGLE_NAVI_PACKAGE = "com.google.android.apps.maps";
    String GOOGLE_NAVI_CAR = "google.navigation:q=%1$s,%2$s&mode=d";
    String GOOGLE_NAVI_FOOT = "google.navigation:q=%1$s,%2$s&mode=w";
}