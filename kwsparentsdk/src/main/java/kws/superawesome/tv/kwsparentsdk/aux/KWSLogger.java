package kws.superawesome.tv.kwsparentsdk.aux;

import android.util.Log;

public class KWSLogger {

    private static boolean loggingEnabled = true;
    private static String PROJECT = "KWS-Parent";

    public static void log (String domain, String message) {
        if (isLoggingEnabled()) {
            String finalMessage = (domain != null ? "[" + domain + "] " : "") + message;
            Log.d(PROJECT, finalMessage);
        }
    }

    public static void error (String domain, String message) {
        if (isLoggingEnabled()) {
            String finalMessage = (domain != null ? "[" + domain + "] " : "") + message;
            Log.e(PROJECT, finalMessage);
        }
    }

    public static void warning (String domain, String message) {
        if (isLoggingEnabled()) {
            String finalMessage = (domain != null ? "[" + domain + "] " : "") + message;
            Log.w(PROJECT, finalMessage);
        }
    }

    public static boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public static void setLoggingEnabled(boolean loggingEnabled) {
        KWSLogger.loggingEnabled = loggingEnabled;
    }
}
