package kws.superawesome.tv.kwsparentsdk.models.kids;

import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.JSONSerializable;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

/**
 * Created by gabriel.coman on 22/12/2016.
 */

public class KWSChildPermissions implements JSONSerializable {

    private boolean accessAddress;
    private boolean accessFirstName;
    private boolean accessLastName;
    private boolean accessEmail;
    private boolean accessStreetAddress;
    private boolean accessCity;
    private boolean accessPostalCode;
    private boolean accessCountry;
    private boolean sendPushNotification;
    private boolean sendNewsletter;
    private boolean enterCompetitions;

    public KWSChildPermissions (String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public KWSChildPermissions (JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    @Override
    public void readFromJson(JSONObject jsonObject) {
        accessAddress = SAJsonParser.getBoolean(jsonObject, "accessAddress", false);
        accessFirstName = SAJsonParser.getBoolean(jsonObject, "accessFirstName", false);
        accessLastName = SAJsonParser.getBoolean(jsonObject, "accessLastName", false);
        accessEmail = SAJsonParser.getBoolean(jsonObject, "accessEmail", false);
        accessStreetAddress = SAJsonParser.getBoolean(jsonObject, "accessStreetAddress", false);
        accessCity = SAJsonParser.getBoolean(jsonObject, "accessCity", false);
        accessPostalCode = SAJsonParser.getBoolean(jsonObject, "accessPostalCode", false);
        accessCountry = SAJsonParser.getBoolean(jsonObject, "accessCountry", false);
        sendPushNotification = SAJsonParser.getBoolean(jsonObject, "sendPushNotification", false);
        sendNewsletter = SAJsonParser.getBoolean(jsonObject, "sendNewsletter", false);
        enterCompetitions = SAJsonParser.getBoolean(jsonObject, "enterCompetitions", false);
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "accessAddress", accessAddress,
                "accessFirstName", accessFirstName,
                "accessLastName", accessLastName,
                "accessEmail", accessEmail,
                "accessStreetAddress", accessStreetAddress,
                "accessCity", accessCity,
                "accessPostalCode", accessPostalCode,
                "accessCountry", accessCountry,
                "sendPushNotification", sendPushNotification,
                "sendNewsletter", sendNewsletter,
                "enterCompetitions", enterCompetitions
        });
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public boolean isAccessAddress() {
        return accessAddress;
    }

    public boolean isAccessFirstName() {
        return accessFirstName;
    }

    public boolean isAccessLastName() {
        return accessLastName;
    }

    public boolean isAccessEmail() {
        return accessEmail;
    }

    public boolean isAccessStreetAddress() {
        return accessStreetAddress;
    }

    public boolean isAccessCity() {
        return accessCity;
    }

    public boolean isAccessPostalCode() {
        return accessPostalCode;
    }

    public boolean isAccessCountry() {
        return accessCountry;
    }

    public boolean isSendPushNotification() {
        return sendPushNotification;
    }

    public boolean isSendNewsletter() {
        return sendNewsletter;
    }

    public boolean isEnterCompetitions() {
        return enterCompetitions;
    }
}
