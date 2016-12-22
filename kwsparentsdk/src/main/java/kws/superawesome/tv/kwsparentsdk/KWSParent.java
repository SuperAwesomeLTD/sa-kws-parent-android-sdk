package kws.superawesome.tv.kwsparentsdk;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import kws.superawesome.tv.kwsparentsdk.aux.KWSLogger;
import kws.superawesome.tv.kwsparentsdk.models.kids.KWSChild;
import kws.superawesome.tv.kwsparentsdk.models.oauth.KWSLoggedUser;
import kws.superawesome.tv.kwsparentsdk.models.parent.KWSParentUser;
import kws.superawesome.tv.kwsparentsdk.services.kids.KWSGetKidsService;
import kws.superawesome.tv.kwsparentsdk.services.kids.KWSGetKidsServiceInterface;
import kws.superawesome.tv.kwsparentsdk.services.oauth.KWSAuthService;
import kws.superawesome.tv.kwsparentsdk.services.oauth.KWSAuthServiceInterface;
import kws.superawesome.tv.kwsparentsdk.services.parent.KWSGetParentService;
import kws.superawesome.tv.kwsparentsdk.services.parent.KWSGetParentServiceInterface;
import kws.superawesome.tv.kwsparentsdk.services.parent.KWSUpdateParentService;
import kws.superawesome.tv.kwsparentsdk.services.parent.KWSUpdateParentServiceInterface;

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
    private KWSGetParentService getParentService;
    private KWSUpdateParentService updateParentService;
    private KWSGetKidsService getKidsService;

    // constructor
    private KWSParent () {
        authService = new KWSAuthService();
        getParentService = new KWSGetParentService();
        updateParentService = new KWSUpdateParentService();
        getKidsService = new KWSGetKidsService();
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

                    // save user locally
                    loggedUser = user;

                    // call proper valid callback
                    if (listener != null) {
                        listener.didAuthUser(loggedUser);
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
        editor.apply();
        loggedUser = null;

        KWSLogger.log("KWS Parent SDK", "Logged out user!");

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Get & Update Parent user data
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void getParentData (Context context, KWSGetParentServiceInterface listener) {
        getParentService.execute(context, listener);
    }

    public void updateParentData (Context context, KWSParentUser updated, KWSUpdateParentServiceInterface listener) {
        updateParentService.execute(context, updated, listener);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Get kids
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void getAllChildren (Context context, KWSGetKidsServiceInterface listener) {
        getKidsService.execute(context, listener);
    }

    public void getActiveChildren (Context context, final KWSGetKidsServiceInterface listener) {
        getKidsService.execute(context, new KWSGetKidsServiceInterface() {
            @Override
            public void gotChildren(List<KWSChild> children) {
                List<KWSChild> result = new ArrayList<>();

                for (KWSChild child : children) {
                    if (child.hasStatus() instanceof Boolean) {
                        if (((Boolean) child.hasStatus())) {
                            result.add(child);
                        }
                    }
                }

                listener.gotChildren(result);
            }
        });
    }

    public void getInactiveChildren (Context context, final KWSGetKidsServiceInterface listener) {
        getKidsService.execute(context, new KWSGetKidsServiceInterface() {
            @Override
            public void gotChildren(List<KWSChild> children) {
                List<KWSChild> result = new ArrayList<>();

                for (KWSChild child : children) {
                    if (child.hasStatus() instanceof Boolean) {
                        if (!((Boolean) child.hasStatus())) {
                            result.add(child);
                        }
                    }
                }

                listener.gotChildren(result);
            }
        });
    }

    public void getPendingChildren (Context context, final KWSGetKidsServiceInterface listener) {
        getKidsService.execute(context, new KWSGetKidsServiceInterface() {
            @Override
            public void gotChildren(List<KWSChild> children) {
                List<KWSChild> result = new ArrayList<>();

                for (KWSChild child : children) {
                    if (child.hasStatus() == null) {
                        result.add(child);
                    }
                }

                listener.gotChildren(result);
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public KWSLoggedUser getLoggedUser () {
        return loggedUser;
    }

    public boolean isUserLogged () {
        return loggedUser != null && loggedUser.isValid();
    }
}
