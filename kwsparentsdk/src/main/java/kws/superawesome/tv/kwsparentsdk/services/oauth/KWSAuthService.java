package kws.superawesome.tv.kwsparentsdk.services.oauth;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import kws.superawesome.tv.kwsparentsdk.aux.KWSLogger;
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
                KWSLogger.log("KWSAuthService", "Authenticated OK for user with ID: " + loggedUser.getMetadata().getUserId() + ".");
                listener.didAuthUser(loggedUser);
            } else {
                KWSLogger.error("KWSAuthService", "User is invalid! Could not auth!");
                listener.didAuthUser(null);
            }
        } else {
            KWSLogger.error("KWSAuthService", "Network operation failed trying to Auth user " + email);
            listener.didAuthUser(null);
        }

    }

    public void execute (Context context, String email, String password, KWSInternalAuthInterface listener) {
        this.email = email;
        this.password = password;
        this.listener = listener != null ? listener : new KWSInternalAuthInterface() {@Override public void didAuthUser(KWSLoggedUser user) {}};

        // create endpoint
        final String endpoint = KWSService.kwsApiURL + getEndpoint();

        // create a new async task
        SAAsyncTask task = new SAAsyncTask(context, new SAAsyncTaskInterface() {
            @Override
            public Object taskToExecute() throws Exception {
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

                // create the final response
                HashMap<String, Object> networkResponse = new HashMap<>();
                networkResponse.put("statusCode", statusCode);
                networkResponse.put("payload", response);

                // return
                return networkResponse;
            }

            @Override
            public void onFinish(Object result) {

                if (result != null && result instanceof HashMap) {

                    // get the hash map
                    HashMap<String, Object> response = (HashMap<String, Object>)result;
                    int status = -1;
                    String payload = null;
                    if (response.containsKey("statusCode")) {
                        status = (int) response.get("statusCode");
                    }
                    if (response.containsKey("payload")) {
                        payload = (String) response.get("payload");
                    }

                    // call the result
                    if (status > -1 && payload != null) {
                        success(status, payload, true);

                    } else {
                        success(status, null, false);
                    }
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
