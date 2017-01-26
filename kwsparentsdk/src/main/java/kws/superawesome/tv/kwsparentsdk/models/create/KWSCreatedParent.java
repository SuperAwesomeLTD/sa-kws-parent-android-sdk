package kws.superawesome.tv.kwsparentsdk.models.create;

import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.SABaseObject;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

public class KWSCreatedParent extends SABaseObject {

    private static final int DEFAULT_ID = -1;
    private static final int DEFAULT_CODE = -1;
    private static final int ERROR_CODE_VALIDATION = 5;
    private static final int ERROR_CODE_DUPLICATE = 10;

    private int id = DEFAULT_ID;
    private int code = DEFAULT_CODE;
    private JSONObject invalid = null;

    public int getId() {
        return id;
    }

    public KWSCreatedParent (String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public KWSCreatedParent (JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    @Override
    public void readFromJson(JSONObject jsonObject) {
        id = SAJsonParser.getInt(jsonObject, "id");
        code = SAJsonParser.getInt(jsonObject, "code", DEFAULT_CODE);
        invalid = SAJsonParser.getJsonObject(jsonObject, "invalid", null);
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] { "id", id });
    }

    @Override
    public boolean isValid() {
        return id > 0 && code == DEFAULT_CODE;
    }

    public boolean isEmailDuplicate () {
        return invalid != null && code == ERROR_CODE_DUPLICATE && invalid.optJSONObject("email") != null;
    }

    public boolean isEmailInvalid() {
        return invalid != null && code == ERROR_CODE_VALIDATION && invalid.optJSONObject("email") != null;
    }

    public boolean isPasswordInvalid() {
        return invalid != null && code == ERROR_CODE_VALIDATION && invalid.optJSONObject("password") != null;
    }
}
