package uel.vteam.belovedhostel.MyInterface;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uel.vteam.belovedhostel.R;

/**
 * Created by Hieu on 12/30/2016.
 */

public class TextValidator {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_PATTERN012X = "^[0][1][2][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]$";  // 0123
    private static final String PHONE_PATTERN016X = "^[0][1][6][2-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]$";  // 0165 366 5350
    private static final String PHONE_PATTERN09X = "^[0][9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]$";     //090
    private static TextValidator instance;
    private static String NULL_ERROR = "";
    private static String INVALID_ERROR = "";


    private Pattern pattern;
    private Matcher matcher;
    private Context context;

    public TextValidator(Context context) {
        this.context = context;
    }


    public static TextValidator getInstance(Context context) {

        if (instance == null){
            instance = new TextValidator(context);
            NULL_ERROR = context.getResources().getString(R.string.type_your_text)+" ";
            INVALID_ERROR =" "+ context.getResources().getString(R.string.invalid);
        }

        return instance;
    }

    public boolean isEmail(String hex) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    public boolean validateEmail(EditText editText) {
        boolean valid = true;

        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError(NULL_ERROR + editText.getHint());
            valid = false;
        } else {
            if (!isEmail(editText.getText().toString())) {
                editText.setError("Email " + INVALID_ERROR);
                valid = false;
            } else {
                editText.setError(null);
            }
        }
        return valid;
    }

    public boolean validateText(EditText editText) {
        boolean valid = true;
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError(NULL_ERROR + editText.getHint());
            valid = false;
        } else {
            editText.setError(null);
        }
        return valid;
    }

    public boolean validateNumber(EditText editText) {
        boolean valid = true;

        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError(NULL_ERROR + editText.getHint());
            valid = false;
        } else {
            if (!TextUtils.isDigitsOnly(editText.getText().toString())) {
                editText.setError(editText.getHint() + INVALID_ERROR);
                valid = false;
            } else {
                editText.setError(null);
            }
        }
        return valid;
    }

    public boolean checkLength(EditText editText, int length) {
        boolean valid = true;
        if (editText.getText().toString().trim().length() < length) {
            editText.setError(context.getResources().getString(R.string.length_require) +" " + length
                    +" "+ context.getResources().getString(R.string.character));
            valid = false;
        } else {
            editText.setError(null);
        }
        return valid;
    }


    public boolean validatePhone(EditText editText) {
        boolean valid = true;
        String phone = editText.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            editText.setError(NULL_ERROR + editText.getHint());
            valid = false;
        } else {
            if (phone.length() < 10) {
                editText.setError(editText.getHint() + INVALID_ERROR);
                valid = false;
            } else {
                CharSequence inputString = phone;
                Character xYz = phone.charAt(1);
                if (xYz.equals('9') == true) {
                    Pattern pattern09X = Pattern.compile(PHONE_PATTERN09X);
                    Matcher matcher09X = pattern09X.matcher(inputString);
                    if (matcher09X.matches()) {
                        editText.setError(null);
                    } else {
                        editText.setError(editText.getHint() + INVALID_ERROR);
                        valid = false;
                    }
                } else {
                    Character xyZ = phone.charAt(2);
                    if (xyZ.equals('2')) {
                        Pattern pattern012X = Pattern.compile(PHONE_PATTERN012X);
                        Matcher matcher012X = pattern012X.matcher(inputString);
                        if (matcher012X.matches()) {
                            editText.setError(null);
                        } else {
                            editText.setError(editText.getHint() + INVALID_ERROR);
                            valid = false;
                        }
                    } else {
                        Pattern pattern016X = Pattern.compile(PHONE_PATTERN016X);
                        Matcher matcher016X = pattern016X.matcher(inputString);
                        if (matcher016X.matches()) {
                            editText.setError(null);
                        } else {
                            editText.setError(editText.getHint() + INVALID_ERROR);
                            valid = false;
                        }
                    }
                }
            }
        }
        return valid;
    }
}