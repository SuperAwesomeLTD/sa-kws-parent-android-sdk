package kws.superawesome.tv.kwsparentsdk;

import android.content.Context;
import android.content.SharedPreferences;

import kws.superawesome.tv.kwsparentsdk.aux.KWSLogger;
import kws.superawesome.tv.kwsparentsdk.models.oauth.KWSLoggedUser;
import kws.superawesome.tv.kwsparentsdk.services.oauth.KWSAuthService;
import kws.superawesome.tv.kwsparentsdk.services.oauth.KWSAuthServiceInterface;

/**
 * Created by gabriel.coman on 21/12/2016.
 */

public class KWSParent {

    // key
    private static final String KWS_PARENT_SDK_PREF = "KWS_PARENT_SDK_PREF";
    private static final String KWS_PARENT_SDK_USER_KEY = "KWS_PARENT_SDK_USER_KEY";

    // singleton instance
    public static KWSParent sdk = new KWSParent();

    // preferences and editor
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    // the current logged user
    private KWSLoggedUser loggedUser;

    // services upon services
    private KWSAuthService authService;

    // constructor
    private KWSParent () {
        authService = new KWSAuthService();
    }

    // setup method
    public void setup (Context context) {
        // get preferences
        preferences = context.getSharedPreferences(KWS_PARENT_SDK_PREF, 0);
        editor = preferences.edit();

        if (preferences.contains(KWS_PARENT_SDK_USER_KEY)) {
            String userJson = preferences.getString(KWS_PARENT_SDK_USER_KEY, "{}");
            KWSLoggedUser tmpUser = new KWSLoggedUser(userJson);

            // if it's still valid
            if (tmpUser.isValid()) {
                loggedUser = tmpUser;
                KWSLogger.log("KWS Parent SDK", "Already found a logged user with ID: " + loggedUser.getMetadata().getUserId() + ".");
            } else {
                KWSLogger.warning("KWS Parent SDK", "Already found a logged user with ID: " + tmpUser.getMetadata().getUserId() + " but it's not valid anymore.");
                editor.remove(KWS_PARENT_SDK_USER_KEY);
                editor.apply();
            }
        } else {
            KWSLogger.warning("KWS Parent SDK", "Could not find a valid user stored in preferences.");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Login & Logout SDK methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void login (final Context context, String email, String password, final KWSAuthServiceInterface listener) {
        authService.execute(context, email, password, new KWSAuthServiceInterface() {
            @Override
            public void didAuthUser(KWSLoggedUser user) {

                if (user != null) {
                    KWSLogger.log("KWS Parent SDK", "Saving user " + user.getMetadata().getUserId() + " to local preferences for further use.");
                    // save user
                    preferences = context.getSharedPreferences(KWS_PARENT_SDK_PREF, 0);
                    editor = preferences.edit();
                    editor.putString(KWS_PARENT_SDK_USER_KEY, user.writeToJson().toString());
                    editor.apply();

                    // call proper valid callback
                    if (listener != null) {
                        listener.didAuthUser(user);
                    }
                } else {
                    // don't save user & call empty callback
                    if (listener != null) {
                        listener.didAuthUser(null);
                    }
                }

            }
        });
    }

    public void logout (Context context) {

        preferences = context.getSharedPreferences(KWS_PARENT_SDK_PREF, 0);
        editor = preferences.edit();
        editor.remove(KWS_PARENT_SDK_USER_KEY);
        loggedUser = null;

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public KWSLoggedUser getLoggedUser() {
        return loggedUser;
    }
}
