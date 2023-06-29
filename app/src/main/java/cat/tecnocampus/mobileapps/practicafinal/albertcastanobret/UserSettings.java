package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret;

import android.app.Application;
import android.content.SharedPreferences;

public class UserSettings extends Application {
    public static final String PREFERENCES = "preferences";

    public static final String CURRENT_LANGUAGE = "currentLanguage";
    public static final String ENGLISH = "en";
    public static final String SPANISH = "es";

    private String currentLanguage;

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(String currentLanguage) {
        this.currentLanguage = currentLanguage;
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENT_LANGUAGE, currentLanguage);
        editor.apply();
    }

}
