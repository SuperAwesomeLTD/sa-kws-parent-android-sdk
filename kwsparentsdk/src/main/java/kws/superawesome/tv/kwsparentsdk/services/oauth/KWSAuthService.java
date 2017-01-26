package kws.superawesome.tv.kwsparentsdk.services.oauth;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import kws.superawesome.tv.kwsparentsdk.models.oauth.KWSLoggedUser;
import kws.superawesome.tv.kwsparentsdk.services.KWSHTTPMethod;
import kws.superawesome.tv.kwsparentsdk.services.KWSService;
import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.sanetwork.asynctask.SAAsyncTask;
import tv.superawesome.lib.sanetwork.asynctask.SAAsyncTaskInterface;
import tv.superawesome.lib.sautils.SAUtils;

public class KWSAuthService extends KWSService {

    private KWSInternalAuthInterface listener;
    private String email;
    private String password;

    @Override
    public String getEndpoint() {
        return "oauth/token";
    }

    @Override
    public KWSHTTPMethod getMethod() {
        return KWSHTTPMethod.POST;
    }

    @Override
    public JSONObject getHeader() {
        return SAJsonParser.newObject(new Object[]{
                "Content-Type", "application/x-www-form-urlencoded",
                "Authorization", "Basic cHBfZnJvbnRlbmQ6cHBfZnJvbnRlbmQ="
        });
    }

    @Override
    public JSONObject getBody() {
        return SAJsonParser.newObject(new Object[]{
                "grant_type", "password",
                "username", email != null ? SAUtils.encodeURL(email) : "",
                "password", password != null ? password : ""
        });
    }

    @Override
    public boolean needsLoggedUser() {
        return false;
    }

    @Override
    public void success(int status, String payload, boolean success) {

        if (success && status == 200 && payload != null) {
            KWSLoggedUser loggedUser = new KWSLoggedUser(payload);
            if (loggedUser.isValid()) {
                Log.d("KWSAuthService", "Authenticated OK for user with ID: " + loggedUser.getMetadata().getUserId() + ".");
                listener.didAuthUser(loggedUser);
            } else {
                Log.e("KWSAuthService", "User is invalid! Could not auth!");
                listener.didAuthUser(null);
            }
        } else {
            Log.e("KWSAuthService", "Network operation failed trying to Auth user " + email);
            listener.didAuthUser(null);
        }

    }

    public void execute (Context context, String email, final String password, KWSInternalAuthInterface listener) {
        this.email = email;
        this.password = password;
        this.listener = listener != null ? listener : new KWSInternalAuthInterface() {@Override public void didAuthUser(KWSLoggedUser user) {}};

        // create endpoint
        final String endpoint = KWSService.kwsApiURL + getEndpoint();

        // create a new async task
        new SAAsyncTask<>(context, new SAAsyncTaskInterface<SANetworkResult>() {
            @Override
            public SANetworkResult taskToExecute() throws Exception {
                int statusCode;
                String response;
                InputStreamReader in;
                OutputStream os = null;

                // create a connection
                URL url = new URL(endpoint);
                HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                // set headers
                Iterator<String> keys = getHeader().keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = getHeader().optString(key);
                    conn.setRequestProperty(key, value);
                }

                // connect
                conn.connect();

                // write body
                List<String> strings = new ArrayList<>();
                Iterator<String> bodykeys = getBody().keys();
                while (bodykeys.hasNext()) {
                    String key = bodykeys.next();
                    String value = getBody().optString(key);
                    strings.add(key +"=" + value);
                }
                String joinedBody = TextUtils.join("&", strings.toArray());
                os = new BufferedOutputStream(conn.getOutputStream());
                os.write(joinedBody.getBytes());
                os.flush();

                // read the result
                statusCode = conn.getResponseCode();
                if (statusCode >= HttpsURLConnection.HTTP_BAD_REQUEST) {
                    in = new InputStreamReader(conn.getErrorStream());
                } else {
                    in = new InputStreamReader(conn.getInputStream());
                }

                // get response
                String line;
                response = "";
                BufferedReader reader = new BufferedReader(in);
                while ((line = reader.readLine()) != null) {
                    response+=line;
                }

                // close the body writer
                if (os != null) {
                    os.close();
                }
                // close the reader
                in.close();

                // disconnect
                conn.disconnect();

                // return
                return new SANetworkResult(statusCode, response);
            }

            @Override
            public void onFinish(SANetworkResult result) {

                if (result.isValid()) {
                    success(result.getStatus(), result.getPayload(), true);
                } else {
                    success(0, null, false);
                }
            }

            @Override
            public void onError() {
                success(0, null, false);
            }
        });
    }
}

/**
 * This private class hold the important details needed when receiving a network saDidGetResponse from
 * a remote server: the HTTP request status (200, 201, 400, 404, etc) and a string saDidGetResponse
 * that will get parsed subsequently.
 */
class SANetworkResult {

    // constants
    private static final int DEFAULT_STATUS = -1;

    // private variables - status & payload
    private int status = DEFAULT_STATUS;
    private String payload;

    /**
     * Custom constructor taking into account all the class members variables
     *
     * @param status    the current request status (200, 201, 400, 404, etc)
     * @param payload   the current request payload. Can be null
     */
    SANetworkResult(int status, String payload) {
        this.status = status;
        this.payload = payload;
    }

    /**
     * Public getter for the "status" member variable
     *
     * @return  the value of the "status" member variable
     */
    int getStatus() {
        return status;
    }

    /**
     * Public getter for the "payload" member variable
     *
     * @return  the value of the "payload" member variable
     */
    String getPayload() {
        return payload;
    }

    /**
     * Method that internally determines if the returned network result has validity
     *
     * @return  true or false depending on the condition
     */
    boolean isValid () {
        return status > DEFAULT_STATUS && payload != null;
    }
}
