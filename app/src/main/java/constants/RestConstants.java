package constants;

/**
 * Created by Przemek on 04.12.2016.
 */

public interface RestConstants {

    // Sciezki Rest API
    String REST_ROOT_PATH = "http://192.168.50.101/POI/Web/app_dev.php/api";
    // Pobieranie tokenu autoryzacji
    String REST_TOKEN_POST = REST_ROOT_PATH + "/tokens";
    // Rejestracja użytkownika
    String REST_REGISTER_POST = REST_ROOT_PATH + "/registers";
    // Pobranie punktu po id
    String REST_POINTS_GET_ID = REST_ROOT_PATH + "/points/%1$d";
    // Dodanie punktu
    String REST_POINTS_POST = REST_ROOT_PATH + "/points";

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
    String JSON_MIMETYPE_KEY = "mimetype";
    String JSON_ADDEDDATE_KEY = "addeddate";
    String JSON_TYPEID_KEY = "typeid";
    String JSON_USERID_KEY = "userid";
}