package kws.superawesome.tv.kwsparentsdk.models.kids;

import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.JSONSerializable;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

/**
 * Created by gabriel.coman on 22/12/2016.
 */

public class KWSChild implements JSONSerializable{

    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String language;
    private String email;
    private boolean hasSetParentEmail;
    private Object status;

    private KWSChildAddress address;
    private KWSChildAppProfile applicationProfile;
    private KWSChildPermissions applicationPermissions;
    private KWSChildPoints points;

    public KWSChild (String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public KWSChild (JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    @Override
    public void readFromJson(JSONObject jsonObject) {
        id = SAJsonParser.getInt(jsonObject, "id");
        username = SAJsonParser.getString(jsonObject, "username");
        firstName = SAJsonParser.getString(jsonObject, "firstName");
        lastName = SAJsonParser.getString(jsonObject, "lastName");
        dateOfBirth = SAJsonParser.getString(jsonObject, "dateOfBirth");
        gender = SAJsonParser.getString(jsonObject, "gender");
        language = SAJsonParser.getString(jsonObject, "language");
        email = SAJsonParser.getString(jsonObject, "email");
        hasSetParentEmail = SAJsonParser.getBoolean(jsonObject, "hasSetParentEmail");
        status = SAJsonParser.get(jsonObject, "status");
        address = new KWSChildAddress(SAJsonParser.getJsonObject(jsonObject, "address"));
        applicationProfile = new KWSChildAppProfile(SAJsonParser.getJsonObject(jsonObject, "applicationProfile"));
        applicationPermissions = new KWSChildPermissions(SAJsonParser.getJsonObject(jsonObject, "applicationPermissions"));
        points = new KWSChildPoints(SAJsonParser.getJsonObject(jsonObject, "points"));
    }

    @Override
    public JSONObject writeToJson() {
        return null;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getLanguage() {
        return language;
    }

    public String getEmail() {
        return email;
    }

    public boolean isHasSetParentEmail() {
        return hasSetParentEmail;
    }

    public Object hasStatus() {
        return status;
    }

    public KWSChildAddress getAddress() {
        return address;
    }

    public KWSChildAppProfile getApplicationProfile() {
        return applicationProfile;
    }

    public KWSChildPermissions getApplicationPermissions() {
        return applicationPermissions;
    }

    public KWSChildPoints getPoints() {
        return points;
    }
}
