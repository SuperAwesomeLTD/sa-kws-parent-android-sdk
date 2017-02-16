package kws.superawesome.tv.kwsparentsdk.services.create;

import android.content.Context;

import org.json.JSONObject;

import kws.superawesome.tv.kwsparentsdk.models.create.KWSCreatedParent;
import kws.superawesome.tv.kwsparentsdk.services.KWSHTTPMethod;
import kws.superawesome.tv.kwsparentsdk.services.KWSService;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

public class KWSCreateParentService extends KWSService {

    private KWSParentCreateUserInterface listener;
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
            listener.didCreateUser(KWSParentCreateUserStatus.Success);
        } else {
            if (createdParent.isPasswordInvalid()) {
                listener.didCreateUser(KWSParentCreateUserStatus.InvalidPassword);
            } else if (createdParent.isEmailInvalid()) {
                listener.didCreateUser(KWSParentCreateUserStatus.InvalidEmail);
            } else if (createdParent.isEmailDuplicate()) {
                listener.didCreateUser(KWSParentCreateUserStatus.DuplicateUsername);
            } else {
                listener.didCreateUser(KWSParentCreateUserStatus.NetworkError);
            }
        }
    }

    public void execute (Context context, String email, String password, KWSParentCreateUserInterface listener) {
        this.email = email;
        this.password = password;
        this.listener = listener != null ? listener : new KWSParentCreateUserInterface() {@Override public void didCreateUser(KWSParentCreateUserStatus status) {}};
        super.execute(context);
    }
}
