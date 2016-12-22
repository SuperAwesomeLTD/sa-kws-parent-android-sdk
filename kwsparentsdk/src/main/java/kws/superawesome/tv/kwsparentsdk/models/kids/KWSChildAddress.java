package kws.superawesome.tv.kwsparentsdk.models.kids;

import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.JSONSerializable;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

/**
 * Created by gabriel.coman on 22/12/2016.
 */

public class KWSChildAddress implements JSONSerializable {

    private String street;
    private String city;
    private String postCode;
    private String country;

    public KWSChildAddress (String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public KWSChildAddress (JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    @Override
    public void readFromJson(JSONObject jsonObject) {
        street = SAJsonParser.getString(jsonObject, "street");
        city = SAJsonParser.getString(jsonObject, "city");
        postCode = SAJsonParser.getString(jsonObject, "postCode");
        country = SAJsonParser.getString(jsonObject, "country");
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "street", street,
                "city", city,
                "postCode", postCode,
                "country", country
        });
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getCountry() {
        return country;
    }


}
