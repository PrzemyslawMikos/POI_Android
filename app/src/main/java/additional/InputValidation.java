package additional;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by Przemysław Mikos on 19.01.2017.
 */

public final class InputValidation {

    private static InputValidation inputValidation;

    public static boolean validteUsername(EditText editText){
        editText.setError(null);
        if (!checkEmpty(editText, "Nazwa użytkownika nie może być pusta"))
            return false;
        if(!checkLength(editText, 3, 30, "Nazwa użytkownika to minimum 3 a maksimum 30 znaków"))
            return false;
        return true;
    }

    public static boolean validatePassword(EditText editText){
        editText.setError(null);
        if (!checkEmpty(editText, "Hasło nie może być puste"))
            return false;
        if(!checkLength(editText, 8, 30, "Hasło to minimum 8 a maksimum 30 znaków"))
            return false;
        return true;
    }

    public static boolean validateNickname(EditText editText){
        editText.setError(null);
        if (!checkEmpty(editText, "Nick nie może być pusty"))
            return false;
        if(!checkLength(editText, 3, 30, "Nick to minimum 8 a maksimum 30 znaków"))
            return false;
        return true;
    }

    public static boolean validateEmail(EditText editText){
        editText.setError(null);
        if (!checkEmpty(editText, "Email nie może być pusty"))
            return false;
        if(!checkLength(editText, 6, 50, "Email to minimum 6 a maksimum 50 znaków"))
            return false;
        if (!checkContainsChars(editText, new char[]{'@', '.'}, "Email musi zawierać znak '@' oraz '.'"))
            return false;
        return true;
    }

    public static boolean validatePhone(EditText editText){
        editText.setError(null);
        if (!checkEmpty(editText, "Telefon nie może być pusty"))
            return false;
        if(!checkLength(editText, 9, 15, "Telefon to minimum 9 a maksimum 15 znaków"))
            return false;
        return true;
    }

    public static boolean validatePointName(EditText editText){
        editText.setError(null);
        if (!checkEmpty(editText, "Nazwa miejsca nie może być pusta"))
            return false;
        if(!checkLength(editText, 3, 60, "Nazwa miejsca to minimum 3 a maksimum 60 znaków"))
            return false;
        return true;
    }

    public static boolean validateLocality(EditText editText){
        editText.setError(null);
        if (!checkEmpty(editText, "Lokalizacja nie może być pusta"))
            return false;
        if(!checkLength(editText, 3, 90, "Lokalizacja to minimum 3 a maksimum 90 znaków"))
            return false;
        return true;
    }

    public static boolean validateDescription(EditText editText){
        editText.setError(null);
        if (!checkEmpty(editText, "Opis nie może być pusty"))
            return false;
        if(!checkLength(editText, 3, 300, "Opis to minimum 3 a maksimum 300 znaków"))
            return false;
        return true;
    }

    private static boolean checkContainsChars(EditText editText, char[] chars, String errorMsg){
        for(char c : chars){
            if(!editText.getText().toString().contains(String.valueOf(c))){
                editText.setError(errorMsg);
                editText.requestFocus();
                return false;
            }
        }
        return true;
    }

    private static boolean checkEmpty(EditText editText, String errorMsg){
        if (TextUtils.isEmpty(editText.getText().toString())){
            editText.setError(errorMsg);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    private static boolean checkLength(EditText editText, int min, int max, String errorMsg){
        if (editText.getText().toString().length() < min || editText.getText().toString().length() > max){
            editText.setError(errorMsg);
            editText.requestFocus();
            return false;
        }
        return true;
    }
}