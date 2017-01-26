package kws.superawesome.tv.kwsparentsdk.models.oauth;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import tv.superawesome.lib.sajsonparser.SABaseObject;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

public class KWSLoggedUser extends SABaseObject {

    private String tokenType;
    private String accessToken;
    private KWSMetadata metadata;

    public KWSLoggedUser (String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public KWSLoggedUser (JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public KWSMetadata getMetadata() {
        return metadata;
    }

    @Override
    public void readFromJson(JSONObject jsonObject) {
        tokenType = SAJsonParser.getString(jsonObject, "token_type");
        accessToken = SAJsonParser.getString(jsonObject, "access_token");
        metadata = processMetadata(accessToken);
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "token_type", tokenType,
                "access_token", accessToken,
                "metadata", metadata.writeToJson()
        });
    }

    @Override
    public boolean isValid() {
        return accessToken != null && metadata != null && metadata.isValid();
    }

    private KWSMetadata processMetadata(String  oauthToken) {

        // get token, if it's valid
        if (oauthToken == null) return null;
        String[] components = oauthToken.split("\\.");
        String tokenO = null;
        if (components.length >= 2) tokenO = components[1];
        if (tokenO == null) return null;

        // get JSON from base64 data
        byte[] data;
        try {
            data = Base64.decode(tokenO, Base64.DEFAULT);
        } catch (IllegalArgumentException e1) {
            try {
                tokenO += "=";
                data = Base64.decode(tokenO, Base64.DEFAULT);
            } catch (IllegalArgumentException e2) {
                try {
                    tokenO += "=";
                    data = Base64.decode(tokenO, Base64.DEFAULT);
                } catch (IllegalArgumentException e3){
                    return null;
                }
            }
        }

        try {
            String jsonData = new String(data, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonData);
            return new KWSMetadata(jsonObject);
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
