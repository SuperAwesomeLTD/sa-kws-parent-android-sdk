package kws.superawesome.tv.kwsparentsdk.services.parent;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kws.superawesome.tv.kwsparentsdk.KWSParent;
import kws.superawesome.tv.kwsparentsdk.aux.KWSLogger;
import kws.superawesome.tv.kwsparentsdk.aux.OkHttpStack;
import kws.superawesome.tv.kwsparentsdk.models.parent.KWSParentUser;
import kws.superawesome.tv.kwsparentsdk.services.KWSHTTPMethod;
import kws.superawesome.tv.kwsparentsdk.services.KWSService;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

/**
 * Created by gabriel.coman on 21/12/2016.
 */

public class KWSUpdateParentService extends KWSService {

    private KWSUpdateParentInterface listener;
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
            KWSLogger.log("KWSUpdateParentService", "Manged to update user!");
        } else {
            KWSLogger.error("KWSUpdateParentService", "Wasn't able to update user! " + payload);
        }
        listener.didUpdateParent(updatedStatus);
    }

    public void execute (Context context, final KWSParentUser updatedParentUser, KWSUpdateParentInterface listener) {
        this.listener = listener != null ? listener : new KWSUpdateParentInterface() {@Override public void didUpdateParent(boolean operationOK) {}};
        this.updatedParentUser = updatedParentUser;
        this.loggedUser = KWSParent.sdk.getLoggedUser();

        final KWSUpdateParentService instance = this;

        // no logged user, no update
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
