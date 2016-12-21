package kws.superawesome.tv.kwsparentsdk.models.oauth;

import org.json.JSONObject;

import kws.superawesome.tv.kwsparentsdk.aux.KWSLogger;
import tv.superawesome.lib.sajsonparser.JSONSerializable;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

public class KWSMetadata implements JSONSerializable {

    private static final int DEFAULT_VAL = -1;

    private int userId = DEFAULT_VAL;
    private String clientId;
    private String scope;
    private int iat = DEFAULT_VAL;
    private int exp = DEFAULT_VAL;
    private String iss;

    public KWSMetadata (String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public KWSMetadata (JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    public int getUserId() {
        return userId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getScope() {
        return scope;
    }

    public int getIat() {
        return iat;
    }

    public int getExp() {
        return exp;
    }

    public String getIss() {
        return iss;
    }

    @Override
    public void readFromJson(JSONObject jsonObject) {
        userId = SAJsonParser.getInt(jsonObject, "userId");
        clientId = SAJsonParser.getString(jsonObject, "clientId");
        scope = SAJsonParser.getString(jsonObject, "scope");
        iat = SAJsonParser.getInt(jsonObject, "iat");
        exp = SAJsonParser.getInt(jsonObject, "exp");
        iss = SAJsonParser.getString(jsonObject, "iss");
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[]{
                "userId", userId,
                "clientId", clientId,
                "scope", scope,
                "iat", iat,
                "exp", exp,
                "iss", iss
        });
    }

    @Override
    public boolean isValid() {
        if (userId == DEFAULT_VAL || iat == DEFAULT_VAL || exp == DEFAULT_VAL) {
            return false;
        } else {
            long now = System.currentTimeMillis() / 1000L;
            long time = now - exp;
            return time < 0;
        }
    }
}
