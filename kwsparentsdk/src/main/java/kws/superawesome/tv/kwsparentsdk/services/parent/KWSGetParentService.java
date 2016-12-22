package kws.superawesome.tv.kwsparentsdk.services.parent;

import android.content.Context;

import org.json.JSONObject;

import kws.superawesome.tv.kwsparentsdk.aux.KWSLogger;
import kws.superawesome.tv.kwsparentsdk.models.parent.KWSParentUser;
import kws.superawesome.tv.kwsparentsdk.services.KWSHTTPMethod;
import kws.superawesome.tv.kwsparentsdk.services.KWSService;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

public class KWSGetParentService extends KWSService {

    private KWSGetParentInterface listener;

    @Override
    public String getEndpoint() {
        return "v1/parents/" + super.loggedUser.getMetadata().getUserId();
    }

    @Override
    public KWSHTTPMethod getMethod() {
        return KWSHTTPMethod.GET;
    }

    @Override
    public JSONObject getHeader() {
        return SAJsonParser.newObject(new Object[] {
                "Authorization", "Bearer " + super.loggedUser.getAccessToken()
        });
    }

    @Override
    public void success(int status, String payload, boolean success) {
        if (status == 200 && success && payload != null) {
            KWSParentUser parent = new KWSParentUser(payload);
            if (parent.isValid()) {
                KWSLogger.log("KWSGetParentService", "Got parent with ID: " + parent.getId() + " and Email: " + parent.getEmail());
                listener.gotParent(parent);
            } else {
                KWSLogger.error("KWSGetParentService", "The parent data I got for user with ID : " + super.loggedUser.getMetadata().getUserId() + " was not valid.");
                listener.gotParent(null);
            }
        } else {
            KWSLogger.error("KWSGetParentService", "There was a network operation trying to get parent data.");
            listener.gotParent(null);
        }
    }

    public void execute (Context context, KWSGetParentInterface listener) {
        this.listener = listener != null ? listener : new KWSGetParentInterface() {@Override public void gotParent(KWSParentUser parent) {}};
        super.execute(context);
    }
}
