package kws.superawesome.tv.kwsparentsdk.services.parent;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kws.superawesome.tv.kwsparentsdk.KWSParent;
import kws.superawesome.tv.kwsparentsdk.models.parent.KWSParentUser;
import kws.superawesome.tv.kwsparentsdk.services.KWSHTTPMethod;
import kws.superawesome.tv.kwsparentsdk.services.KWSService;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

/**
 * Created by gabriel.coman on 21/12/2016.
 */

public class KWSUpdateParentService extends KWSService {

    private KWSParentUpdateUserInterface listener;
    private KWSParentUser updatedParentUser;

    @Override
    public String getEndpoint() {
        return "v1/parents/" + super.loggedUser.getMetadata().getUserId();
    }

    @Override
    public KWSHTTPMethod getMethod() {
        return KWSHTTPMethod.PATCH;
    }

    @Override
    public JSONObject getHeader() {
        return SAJsonParser.newObject(new Object[] {
                "Content-Type", "application/json",
                "Authorization", "Bearer " + super.loggedUser.getAccessToken()
        });
    }

    @Override
    public JSONObject getBody() {
        return updatedParentUser.writeUpdateJson();
    }

    @Override
    public void success(int status, String payload, boolean success) {
        boolean updatedStatus = success && status == 204;
        if (updatedStatus) {
            Log.d("KWSUpdateParentService", "Manged to updateUser user!");
        } else {
            Log.e("KWSUpdateParentService", "Wasn't able to updateUser user! " + payload);
        }
        listener.didUpdateUser(updatedStatus);
    }

    public void execute (Context context, final KWSParentUser updatedParentUser, KWSParentUpdateUserInterface listener) {
        this.listener = listener != null ? listener : new KWSParentUpdateUserInterface() {@Override public void didUpdateUser(boolean operationOK) {}};
        this.updatedParentUser = updatedParentUser;
        this.loggedUser = KWSParent.sdk.getLoggedUser();

        final KWSUpdateParentService instance = this;

        // no logged user, no updateUser
        if (needsLoggedUser() && loggedUser == null) {
            instance.success(0, null, false);
            return;
        }

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context, new OkHttpStack());
        String url = KWSService.kwsApiURL + getEndpoint();


        // instantiate the request
        StringRequest request = new StringRequest(Request.Method.PATCH, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                instance.success(204, response, true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                instance.success(0, error.toString(), false);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return jsonToMap(getHeader());
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return KWSUpdateParentService.this.getBody().toString().getBytes();
            }
        };

        // add and execute
        queue.add(request);
    }

    private Map<String, String> jsonToMap (JSONObject jsonObject) {
        Map<String, String> map = new HashMap<>();

        Iterator<String> keysIterator = jsonObject.keys();
        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            try {
                Object val = jsonObject.get(key);

                if (val instanceof String) {
                    map.put(key, (String) val);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return map;
    }
}

class OkHttpStack extends HurlStack {
    private final OkUrlFactory mFactory;

    OkHttpStack() {
        this(new OkHttpClient());
    }

    private OkHttpStack(OkHttpClient client) {
        if (client == null) {
            throw new NullPointerException("Client must not be null.");
        }
        mFactory = new OkUrlFactory(client);
    }

    @Override protected HttpURLConnection createConnection(URL url) throws IOException {
        return mFactory.open(url);
    }
}
