package kws.superawesome.tv.kwsparentsdk.services;

import android.content.Context;

import org.json.JSONObject;

import kws.superawesome.tv.kwsparentsdk.KWSParent;
import kws.superawesome.tv.kwsparentsdk.models.oauth.KWSLoggedUser;
import tv.superawesome.lib.sanetwork.request.SANetwork;
import tv.superawesome.lib.sanetwork.request.SANetworkInterface;

public class KWSService implements KWSInterface {

    // protected request vars
    protected static final String kwsApiURL = "https://parentsapi.demo.superawesome.tv/";
    protected KWSLoggedUser loggedUser;
    private SANetwork network;

    public KWSService () {
        network = new SANetwork();
    }

    @Override
    public String getEndpoint() {
        return null;
    }

    @Override
    public KWSHTTPMethod getMethod() {
        return KWSHTTPMethod.GET;
    }

    @Override
    public JSONObject getQuery() {
        return new JSONObject();
    }

    @Override
    public JSONObject getHeader() {
        return new JSONObject();
    }

    @Override
    public JSONObject getBody() {
        return new JSONObject();
    }

    @Override
    public boolean needsLoggedUser() {
        return true;
    }

    @Override
    public void success(int status, String payload, boolean success) {

    }

    protected void execute(Context context) {
        this.loggedUser = KWSParent.sdk.getLoggedUser();

        if (needsLoggedUser() && loggedUser == null) {
            success(0, null, false);
        } else {
            final KWSService instance = this;

            switch (getMethod()) {
                case GET: {
                    network.sendGET(context, kwsApiURL + getEndpoint(), getQuery(), getHeader(), new SANetworkInterface() {
                        @Override
                        public void saDidGetResponse(int status, String payload, boolean success) {
                            instance.success(status, payload, success);
                        }
                    });
                    break;
                }
                case POST: {
                    network.sendPOST(context, kwsApiURL + getEndpoint(), getQuery(), getHeader(), getBody(), new SANetworkInterface() {
                        @Override
                        public void saDidGetResponse(int status, String payload, boolean success) {
                            instance.success(status, payload, success);
                        }
                    });
                    break;
                }
                case PUT: {
                    network.sendPUT(context, kwsApiURL + getEndpoint(), getQuery(), getHeader(), getBody(), new SANetworkInterface() {
                        @Override
                        public void saDidGetResponse(int status, String payload, boolean success) {
                            instance.success(status, payload, success);
                        }
                    });
                    break;
                }
                case PATCH:{
                    // do nothing
                    break;
                }
            }
        }
    }
}
