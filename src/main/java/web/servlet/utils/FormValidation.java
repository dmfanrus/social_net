package web.servlet.utils;
import model.Credentials;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Михаил on 06.01.2017.
 */
public class FormValidation {

    private static final long ACCESSAGE = 14;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String LOGIN_PATTERN =
            "^[A-Za-z][A-Za-z0-9]*?([-_][A-Za-z0-9]+){0,2}$";

    private static final String FULLNAME_PATTERN =
            "^[a-zA-Zа-яА-ЯёЁ'][a-zA-Z-а-яА-ЯёЁ']+[a-zA-Zа-яА-ЯёЁ']?$";

    private Map<String,String> errors = new HashMap<>();

    public Map<String,String> getErrors() {
        return errors;
    }

    public void setError(String fieldName,String errorName) {
        if(!errors.containsKey(fieldName))
            errors.put(fieldName,errorName);
    }

    public boolean isValid(){
        return errors.isEmpty();
    }

    public boolean validateString(String fieldName, String fieldValue){
        if (fieldValue==null || fieldValue.isEmpty()){
            setError(fieldName, "empty");
            return false;
        }
        return true;
    }

    public void validateStrings(HashMap<String,String> fields){
        fields.forEach((fieldName,fieldValue) -> validateString(fieldName,fieldValue));
    }

    public void validateCredentials(Credentials credentials){
        validateString("password",credentials.getPassword());
        if(validateString("login",credentials.getLogin())){
            if(!credentials.getLogin().matches(LOGIN_PATTERN)) {
                setError("login", "loginWrong");
            }
        }
    }

    public void validateEmail(String email){
        if(validateString("email",email)){
            if(!email.matches(EMAIL_PATTERN)) {
                setError("email", "emailWrong");
            }
        }
    }

    public void validateFullName(String fullname){
        if(validateString("fullName",fullname)){
            if(!fullname.matches(FULLNAME_PATTERN)) {
                setError("fullName", "fullNameWrong");
            }
        }
    }

    public void validateDate(String dateOfBirth){
        if(validateString("dateOfBirth",dateOfBirth)) {
            long age = ChronoUnit.YEARS.between(LocalDate.parse(dateOfBirth), LocalDate.now());
            if (age < ACCESSAGE) {
                setError("dateOfBirth", "tooYoung");
            }
        }
    }

    public void clearValidate(){
        errors.clear();
    }

}
