package constants;

/**
 * Created by Przemek on 04.12.2016.
 */

public interface RestConstants {

    // Główna sciezka hosta
    String REST_MAIN_HOST_PATH = "http://192.168.1.6";
    // Sciezki Rest API
    String REST_ROOT_PATH = REST_MAIN_HOST_PATH + "/POI/Web/app_dev.php/api";
    // Sciezka zdjęć
    String REST_IMAGE_PATH = REST_MAIN_HOST_PATH +"/POI/Web/uploads";
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
    // Pobranie punktu według kryteriów, 1 = id typu, 2 = miejscowosc, 3 = limit, 4 = offset
    String REST_POINTS_CRITERIA = REST_ROOT_PATH + "/points/%1$d/%2$s/%3$d/%4$d";
    // Dodanie punktu
    String REST_POINTS_POST = REST_ROOT_PATH + "/point";
    // Pobranie wszystkich typow
    String REST_TYPES_GET = REST_ROOT_PATH + "/types";
    // Pobranie użytkownika po id
    String REST_USERS_GET_ID = REST_ROOT_PATH + "/user/%1$d";
    // Dodanie oceny
    String REST_RATINGS_POST = REST_ROOT_PATH + "/rating";

    // Parametr pobrania punktów z każdej miejscowości
    String DEFAULT_POINTS_LOCALITY_PARAM = "*";

    // Parametr naglowka zadania
    String REQUEST_HEADER_BEARER = "Bearer %1$s";
    String REQUEST_HEADER_AUTHORIZATION = "Authorization";

    // Atrybuty odpowiedzi i żądań JSON
    String JSON_TOKEN_KEY = "token";
    String JSON_STATUS_KEY = "STATUS";
    String JSON_USERNAME_KEY = "username";
    String JSON_PASSWORD_KEY = "password";
    String JSON_NICKNAME_KEY = "nickname";
    String JSON_EMAIL_KEY = "email";
    String JSON_PHONE_KEY = "phone";
    String JSON_ID_KEY = "id";
    String JSON_LONGITUDE_KEY = "longitude";
    String JSON_LATITUDE_KEY = "latitude";
    String JSON_RATING_KEY = "rating";
    String JSON_NAME_KEY = "name";
    String JSON_LOCALITY_KEY = "locality";
    String JSON_DESCRIPTION_KEY = "description";
    String JSON_PICTURE_KEY = "picture";
    String JSON_IMAGE_KEY = "image";
    String JSON_MIMETYPE_KEY = "mimetype";
    String JSON_ADDEDDATE_KEY = "addeddate";
    String JSON_CREATIONDATE_KEY = "creationdate";
    String JSON_TYPEID_KEY = "typeid";
    String JSON_USERID_KEY = "userid";
    String JSON_POINTID_KEY = "pointid";
    String JSON_PERMISSIONID_KEY = "permissionid";
    String JSON_FIRSTLOGIN_KEY = "firstlogin";
    String JSON_UNBLOCKED_KEY = "unblocked";

    // Informacje zwracane przez serwer
    String STATUS_OK = "OK";
    String STATUS_WRONG_PARAMS = "WRONG_PARAMS";
    String STATUS_BAD_CREDENTIALS = "BAD_CREDENTIALS";
    String STATUS_USER_BLOCKED = "USER_BLOCKED";
    String STATUS_USERNAME_EXIST = "USERNAME_EXIST";
    String STATUS_EMAIL_EXIST = "EMAIL_EXIST";
    String STATUS_INTERNAL_SERVER_ERROR = "INTERNAL_ERROR";
    String STATUS_NOT_FOUND = "NOT_FOUND";
    String STATUS_TOKEN_EXPIRED = "TOKEN_EXPIRED";
    String STATUS_BAD_TOKEN = "BAD_TOKEN";
    String STATUS_NEW = "NEW";
    String STATUS_UPDATED = "UPDATED";
}