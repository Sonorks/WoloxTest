package wolox.test.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorUtil {
    public static boolean validateStringUsingRegexp(String string, String regexp){
        return string != null && !string.isEmpty() && string.matches(regexp);
    }

    public static boolean validateUserOrAlbumId(String id) {
        return validateStringUsingRegexp(id, "^[0-9]{1,15}$") && Integer.parseInt(id)>0;
    }

    public static boolean validateEmail(String email){
        return validateStringUsingRegexp(email, "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
    }

    public static boolean validateCommentName(String name){
        return validateStringUsingRegexp(name, "[A-Za-z�-�\\u00f1\\u00d10-9_ -]{1,100}$");
    }
}
