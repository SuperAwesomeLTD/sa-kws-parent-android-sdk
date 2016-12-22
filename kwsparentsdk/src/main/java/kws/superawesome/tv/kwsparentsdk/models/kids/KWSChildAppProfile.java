package kws.superawesome.tv.kwsparentsdk.models.kids;

import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.JSONSerializable;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

/**
 * Created by gabriel.coman on 22/12/2016.
 */

public class KWSChildAppProfile implements JSONSerializable {

    private String username;
    private int customField1;
    private int customField2;
    private int avatarId;

    public KWSChildAppProfile (String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public KWSChildAppProfile (JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    @Override
    public void readFromJson(JSONObject jsonObject) {
        username = SAJsonParser.getString(jsonObject, "username");
        customField1 = SAJsonParser.getInt(jsonObject, "customField1");
        customField2 = SAJsonParser.getInt(jsonObject, "customField2");
        avatarId = SAJsonParser.getInt(jsonObject, "avatarId");
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "username", username,
                "customField1", customField1,
                "customField2", customField2,
                "avatarId", avatarId
        });
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public String getUsername() {
        return username;
    }

    public int getCustomField1() {
        return customField1;
    }

    public int getCustomField2() {
        return customField2;
    }

    public int getAvatarId() {
        return avatarId;
    }


}
