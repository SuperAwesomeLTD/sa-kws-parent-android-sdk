package kws.superawesome.tv.kwsparentsdk.services.create;

import android.content.Context;

import org.json.JSONObject;

import kws.superawesome.tv.kwsparentsdk.models.create.KWSCreatedParent;
import kws.superawesome.tv.kwsparentsdk.services.KWSHTTPMethod;
import kws.superawesome.tv.kwsparentsdk.services.KWSService;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

public class KWSCreateParentService extends KWSService {

    private KWSCreateParentInterface listener;
    private String email;
    private String password;

    @Override
    public String getEndpoint() {
        return "v1/parents";
    }

    @Override
    public KWSHTTPMethod getMethod() {
        return KWSHTTPMethod.POST;
    }

    @Override
    public JSONObject getHeader() {
        return SAJsonParser.newObject(new Object[] {
                "Content-Type", "application/json"
        });
    }

    @Override
    public boolean needsLoggedUser() {
        return false;
    }

    @Override
    public JSONObject getBody() {
        return SAJsonParser.newObject(new Object[] {
                "email", email,
                "password", password
        });
    }

    @Override
    public void success(int status, String payload, boolean success) {

        KWSCreatedParent createdParent = new KWSCreatedParent(payload);

        if (createdParent.isValid()) {
            listener.didCreateParent(KWSCreateParentStatus.CREATED);
        } else {
            if (createdParent.isPasswordInvalid()) {
                listener.didCreateParent(KWSCreateParentStatus.INVALID_PASSWORD);
            } else if (createdParent.isEmailInvalid()) {
                listener.didCreateParent(KWSCreateParentStatus.INVALID_EMAIL);
            } else if (createdParent.isEmailDuplicate()) {
                listener.didCreateParent(KWSCreateParentStatus.DUPLICATE);
            } else {
                listener.didCreateParent(KWSCreateParentStatus.NETWORK_ERROR);
            }
        }
    }

    public void execute (Context context, String email, String password, KWSCreateParentInterface listener) {
        this.email = email;
        this.password = password;
        this.listener = listener != null ? listener : new KWSCreateParentInterface() {@Override public void didCreateParent(KWSCreateParentStatus status) {}};
        super.execute(context);
    }
}
