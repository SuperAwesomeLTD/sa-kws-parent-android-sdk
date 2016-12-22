package kws.superawesome.tv.kwsparentsdk.services.kids;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kws.superawesome.tv.kwsparentsdk.models.kids.KWSChild;
import kws.superawesome.tv.kwsparentsdk.models.parent.KWSParentUser;
import kws.superawesome.tv.kwsparentsdk.services.KWSHTTPMethod;
import kws.superawesome.tv.kwsparentsdk.services.KWSService;
import kws.superawesome.tv.kwsparentsdk.services.parent.KWSGetParentServiceInterface;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

/**
 * Created by gabriel.coman on 22/12/2016.
 */

public class KWSGetKidsService extends KWSService {

    private KWSGetKidsServiceInterface listener;

    @Override
    public KWSHTTPMethod getMethod() {
        return KWSHTTPMethod.GET;
    }

    @Override
    public String getEndpoint() {
        return "v1/parents/" + super.loggedUser.getMetadata().getUserId() + "/children";
    }

    @Override
    public JSONObject getHeader() {
        return SAJsonParser.newObject(new Object[] {
                "Authorization", "Bearer " + super.loggedUser.getAccessToken()
        });
    }

    @Override
    public void success(int status, String payload, boolean success) {
        if (success && status == 200) {
            List<KWSChild> list = new ArrayList<>();

            JSONArray array = SAJsonParser.newArray(payload);
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject object = (JSONObject) array.get(i);
                    KWSChild child = new KWSChild(object);
                    list.add(child);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // send data
            listener.gotChildren(list);

        } else {
            listener.gotChildren(new ArrayList<KWSChild>());
        }
    }

    public void execute (Context context, KWSGetKidsServiceInterface listener) {
        this.listener = listener != null ? listener : new KWSGetKidsServiceInterface() {@Override public void gotChildren(List<KWSChild> children) {}};
        super.execute(context);
    }
}
