package kws.superawesome.tv.kwsparentsdk.models.parent;

import org.json.JSONException;
import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.JSONSerializable;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

/**
 * Created by gabriel.coman on 21/12/2016.
 */

public class KWSParentUser implements JSONSerializable {

    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String city;
    private String postalCode;
    private String streetAddress;
    private String country;
    private String countryName;
    private String language;
    private String languageName;
    private String stripeChargeId;
    private boolean newsletterEnabled;
    private boolean splashpageVisited;
    private String createdAt;

    public KWSParentUser(String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public KWSParentUser(JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    @Override
    public void readFromJson(JSONObject jsonObject) {
        id = SAJsonParser.getInt(jsonObject, "id");
        email = SAJsonParser.getString(jsonObject, "email");
        firstName = SAJsonParser.getString(jsonObject, "firstName");
        lastName = SAJsonParser.getString(jsonObject, "lastName");
        dateOfBirth = SAJsonParser.getString(jsonObject, "dateOfBirth");
        gender = SAJsonParser.getString(jsonObject, "gender");
        city = SAJsonParser.getString(jsonObject, "city");
        postalCode = SAJsonParser.getString(jsonObject, "postalCode");
        streetAddress = SAJsonParser.getString(jsonObject, "streetAddress");
        country = SAJsonParser.getString(jsonObject, "country");
        countryName = SAJsonParser.getString(jsonObject, "countryName");
        language = SAJsonParser.getString(jsonObject, "language");
        languageName = SAJsonParser.getString(jsonObject, "languageName");
        stripeChargeId = SAJsonParser.getString(jsonObject, "stripeChargeId");
        newsletterEnabled = SAJsonParser.getBoolean(jsonObject, "newsletterEnabled", false);
        splashpageVisited = SAJsonParser.getBoolean(jsonObject, "splashpageVisited", false);
        createdAt = SAJsonParser.getString(jsonObject, "createdAt");
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "id", id,
                "email", email,
                "firstName", firstName,
                "lastName", lastName,
                "dateOfBirth", dateOfBirth,
                "gender", gender,
                "city", city,
                "postalCode", postalCode,
                "streetAddress", streetAddress,
                "country", country,
                "countryName", countryName,
                "language", language,
                "languageName", languageName,
                "stripeChargeId", stripeChargeId,
                "newsletterEnabled", newsletterEnabled,
                "splashpageVisited", splashpageVisited,
                "createdAt", createdAt
        });
    }

    @Override
    public boolean isValid() {
        return id > 0 && email != null;
    }

    public JSONObject writeUpdateJson () {
        JSONObject jsonObject = new JSONObject();

        if (email != null) {
            try {
                jsonObject.put("email", email);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (firstName != null) {
            try {
                jsonObject.put("firstName", firstName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (lastName != null) {
            try {
                jsonObject.put("lastName", lastName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (dateOfBirth != null) {
            try {
                jsonObject.put("dateOfBirth", dateOfBirth);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (gender != null) {
            try {
                jsonObject.put("gender", gender);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (city != null) {
            try {
                jsonObject.put("city", city);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (postalCode != null) {
            try {
                jsonObject.put("jsonObject", jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (streetAddress != null) {
            try {
                jsonObject.put("streetAddress", streetAddress);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (country != null) {
            try {
                jsonObject.put("country", country);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (language != null) {
            try {
                jsonObject.put(language, language);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        try {
//            jsonObject.put("newsletterEnabled", newsletterEnabled);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            jsonObject.put("splashpageVisited", splashpageVisited);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return jsonObject;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
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

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getLanguage() {
        return language;
    }

    public String getLanguageName() {
        return languageName;
    }

    public String getStripeChargeId() {
        return stripeChargeId;
    }

    public boolean isNewsletterEnabled() {
        return newsletterEnabled;
    }

    public boolean isSplashpageVisited() {
        return splashpageVisited;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String fname) {
        this.firstName = fname;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setStripeChargeId(String stripeChargeId) {
        this.stripeChargeId = stripeChargeId;
    }
}
