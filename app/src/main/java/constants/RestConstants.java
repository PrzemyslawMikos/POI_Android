package constants;

/**
 * Created by Przemek on 04.12.2016.
 */

public interface RestConstants {

    // Sciezki Rest API
    String REST_ROOT_PATH = "http://192.168.1.2/POI/Web/app_dev.php/api";
    // Sciezka zdjęć
    String REST_IMAGE_PATH = "http://192.168.1.2/POI/Web/uploads";
    // Sciezka zdjęć typów
    String REST_TYPES_IMAGE = REST_IMAGE_PATH + "/types/%1$s";
    // Sciezka zdjęć punktów
    String REST_POINTS_IMAGE = REST_IMAGE_PATH + "/points/%1$s";
    // Pobieranie tokenu autoryzacji
    String REST_TOKEN_POST = REST_ROOT_PATH + "/token";
    // Rejestracja użytkownika
    String REST_REGISTER_POST = REST_ROOT_PATH + "/register";
    // Pobranie punktu po id
    String REST_POINTS_GET_ID = REST_ROOT_PATH + "/point/%1$d";
    // Dodanie punktu
    String REST_POINTS_POST = REST_ROOT_PATH + "/point";
    // Pobranie wszystkich typow
    String REST_TYPES_GET = REST_ROOT_PATH + "/types";
    // Pobranie użytkownika po id
    String REST_USERS_GET_ID = REST_ROOT_PATH + "/user/%1$d";

    // Parametr naglowka zadania
    String REQUEST_HEADER_BEARER = "Bearer %1$s";
    String REQUEST_HEADER_AUTHORIZATION = "Authorization";

    // Atrybuty odpowiedzi i żądań JSON
    String JSON_TOKEN_KEY = "token";
    String JSON_STATUS_KEY = "status";
    String JSON_USERNAME_KEY = "username";
    String JSON_PASSWORD_KEY = "password";
    String JSON_NICKNAME_KEY = "nickname";
    String JSON_EMAIL_KEY = "email";
    String JSON_PHONE_KEY = "phone";
    String JSON_ID_KEY = "id";
    String JSON_LONGITUDE_KEY = "longitude";
    String JSON_LATITUDE_KEY = "latitude";
    String JSON_NAME_KEY = "name";
    String JSON_DESCRIPTION_KEY = "description";
    String JSON_PICTURE_KEY = "picture";
    String JSON_IMAGE_KEY = "image";
    String JSON_MIMETYPE_KEY = "mimetype";
    String JSON_ADDEDDATE_KEY = "addeddate";
    String JSON_CREATIONDATE_KEY = "creationdate";
    String JSON_TYPEID_KEY = "typeid";
    String JSON_USERID_KEY = "userid";
    String JSON_PERMISSIONID_KEY = "permissionid";
    String JSON_FIRSTLOGIN_KEY = "firstlogin";
    String JSON_UNBLOCKED_KEY = "unblocked";
}