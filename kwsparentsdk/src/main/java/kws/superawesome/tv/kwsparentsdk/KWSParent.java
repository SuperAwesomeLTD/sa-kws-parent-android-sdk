package kws.superawesome.tv.kwsparentsdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import kws.superawesome.tv.kwsparentsdk.models.oauth.KWSLoggedUser;
import kws.superawesome.tv.kwsparentsdk.models.parent.KWSParentUser;
import kws.superawesome.tv.kwsparentsdk.services.create.KWSParentCreateUserInterface;
import kws.superawesome.tv.kwsparentsdk.services.create.KWSCreateParentService;
import kws.superawesome.tv.kwsparentsdk.services.oauth.KWSParentLoginUserInterface;
import kws.superawesome.tv.kwsparentsdk.services.oauth.KWSInternalAuthInterface;
import kws.superawesome.tv.kwsparentsdk.services.oauth.KWSAuthService;
import kws.superawesome.tv.kwsparentsdk.services.parent.KWSParentGetUserInterface;
import kws.superawesome.tv.kwsparentsdk.services.parent.KWSGetParentService;
import kws.superawesome.tv.kwsparentsdk.services.parent.KWSParentUpdateUserInterface;
import kws.superawesome.tv.kwsparentsdk.services.parent.KWSUpdateParentService;

public class KWSParent {

    /**
     * Basically constants used by the preferences
     */
    private static final String KWS_PARENT_SDK_PREF = "KWS_PARENT_SDK_PREF";
    private static final String KWS_PARENT_SDK_USER_KEY = "KWS_PARENT_SDK_USER_KEY";

    /**
     * Singleton instance variable
     */
    public static KWSParent sdk = new KWSParent();

    /**
     * Shared preferences object
     */
    private SharedPreferences preferences;

    /**
     * Current in-memory logged user. This will either get set (or null-ed) as part of a
     * loginUser / logoutUser operation, or if the SDK user calls "setup" at the start of the app
     * lifecycle and that in turn finds out there is a user in shared preferences.
     */
    private KWSLoggedUser loggedUser;

    /**
     * Local service variables.
     */
    private KWSCreateParentService createParentService;
    private KWSAuthService authService;
    private KWSGetParentService getParentService;
    private KWSUpdateParentService updateParentService;

    /**
     * Private constructor for this singleton class
     * It initiates all the services needed
     */
    private KWSParent () {
        createParentService = new KWSCreateParentService();
        authService = new KWSAuthService();
        getParentService = new KWSGetParentService();
        updateParentService = new KWSUpdateParentService();
    }

    /**
     * SDK Setup method - basically needed in order to get a previously logged user, if he exists
     * @param context - current context
     */
    public void setup (Context context) {
        // get preferences
        preferences = context.getSharedPreferences(KWS_PARENT_SDK_PREF, 0);

        if (preferences.contains(KWS_PARENT_SDK_USER_KEY)) {
            String userJson = preferences.getString(KWS_PARENT_SDK_USER_KEY, "{}");
            KWSLoggedUser tmpUser = new KWSLoggedUser(userJson);

            // if it's still valid
            if (tmpUser.isValid()) {
                loggedUser = tmpUser;
                Log.w("KWS Parent SDK", "Already found a logged user with ID: " + loggedUser.getMetadata().getUserId() + ".");
            } else {
                Log.w("KWS Parent SDK", "Already found a logged user with ID: " + tmpUser.getMetadata().getUserId() + " but it's not valid anymore.");
                preferences.edit().remove(KWS_PARENT_SDK_USER_KEY).apply();
            }
        } else {
            Log.w("KWS Parent SDK", "Could not find a valid user stored in preferences.");
        }
    }

    public void reset () {
        loggedUser = null;
    }

    /**
     * Method that creates a new parent user
     * @param context - the current context
     * @param parentEmail - an email address for the new parent user.
     *              If it's invalid, the method will forward a listener call with
     *              InvalidEmail as status parameter.
     *              If it's a duplicate email, the method will forward a listener call with
     *              DuplicateUsername as status parameter.
     * @param password - a valid password for the new parent user. If it's invalid (e.g. less
     *                 than 8 characters), the method will forward a listener call with
     *                 InvalidPassword as status parameter
     * @param listener - the listener used as callback for the method. Will always need one
     *                 method implementation "didCreateUser" with a status parameter that can
     *                 have one of the following enum values:
     *                      - Success
     *                      - DuplicateUsername
     *                      - InvalidEmail
     *                      - InvalidPassword
     *                      - NetworkError
     */
    public void createUser (Context context, String parentEmail, String password, KWSParentCreateUserInterface listener) {
        createParentService.execute(context, parentEmail, password, listener);
    }

    /**
     * Method that logs in a parent user to Kids Web Services and saves the logged user data
     * for 24h on the Android device
     * @param context - the current context
     * @param parentEmail - a valid parent's user email
     * @param password - a valid parent's user password
     * @param listener - the listener used as callback for the method. Will always need one
     *                 method implementation for "didLoginUser" with a boolean parameter indicating
     *                 success or failure.
     */
    public void loginUser (final Context context, String parentEmail, String password, final KWSParentLoginUserInterface listener) {
        authService.execute(context, parentEmail, password, new KWSInternalAuthInterface() {
            @Override
            public void didAuthUser(KWSLoggedUser user) {

                if (user != null) {
                    Log.d("KWS Parent SDK", "Saving user " + user.getMetadata().getUserId() + " to local preferences for further use.");
                    // save user
                    preferences = context.getSharedPreferences(KWS_PARENT_SDK_PREF, 0);
                    preferences.edit().putString(KWS_PARENT_SDK_USER_KEY, user.writeToJson().toString()).apply();

                    // save user locally
                    loggedUser = user;

                    // call proper valid callback
                    if (listener != null) {
                        listener.didLoginUser(true);
                    }
                } else {
                    // don't save user & call empty callback
                    if (listener != null) {
                        listener.didLoginUser(false);
                    }
                }

            }
        });
    }

    /**
     * Method that logs out an user from the current session.
     * Basically it just deletes whatever user exists in the shared preferences and the current
     * in memory user.
     * @param context - the current context
     */
    public void logoutUser (Context context) {

        preferences = context.getSharedPreferences(KWS_PARENT_SDK_PREF, 0);
        preferences.edit().remove(KWS_PARENT_SDK_USER_KEY).apply();
        loggedUser = null;

        Log.w("KWS Parent SDK", "Logged out user!");

    }

    /**
     * Method that gets parent data for a logged user. Will return an error in case there is no
     * logged user.
     * @param context - the current context
     * @param listener - the listener used as callback for the method. Will always need one
     *                 method implementation for "didGetUser", which will in turn have one
     *                 parameter of type KWSParentUser, which is a model class that contains all
     *                 parent details.
     */
    public void getUser (Context context, KWSParentGetUserInterface listener) {
        getParentService.execute(context, listener);
    }

    /**
     * Method that updates data for a logged user. Will return an error in case there is no
     * logged user.
     * @warning This method uses the "Volley" library for the moment, since vanilla
     * HttpConnection doesn't support the "PATCH" request type.
     * @param context - the current context
     * @param parent - a KWSParentUser type model that contains the fields to be updated. All
     *                fields that are set to null are ignored.
     * @param listener - the listener used as callback for the method. Will always need one
     *                 method implementation for "didUpdateUser", which will in turn have one
     *                 parameter of type boolean indicating if the operation was successful or not.
     */
    public void updateUser (Context context, KWSParentUser parent, KWSParentUpdateUserInterface listener) {
        updateParentService.execute(context, parent, listener);
    }

    /**
     * Get the current logged user data.
     * @return returns the current logged user, as a model of type KWSLogged user.
     */
    public KWSLoggedUser getLoggedUser () {
        return loggedUser;
    }

    /**
     * Returns whether there is a logged user, and that user is valid
     * @return true or false
     */
    public boolean isUserLogged () {
        return loggedUser != null && loggedUser.isValid();
    }

    /**
     * Aux method returning the current SDK version
     * @return a string representing the SDK version
     */
    public String getVersion () {
        return "android-1.2.0";
    }
}
