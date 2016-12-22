package kws.superawesome.tv.kwsparentsdk.models.kids;

import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.JSONSerializable;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

/**
 * Created by gabriel.coman on 22/12/2016.
 */

public class KWSChildPoints implements JSONSerializable {

    private int totalReceived;
    private int total;
    private int totalPointsReceivedInCurrentApp;
    private int availableBalance;
    private int pending;

    public KWSChildPoints (String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public KWSChildPoints (JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    @Override
    public void readFromJson(JSONObject jsonObject) {
        totalReceived = SAJsonParser.getInt(jsonObject, "totalReceived");
        total = SAJsonParser.getInt(jsonObject, "total");
        totalPointsReceivedInCurrentApp = SAJsonParser.getInt(jsonObject, "totalPointsReceivedInCurrentApp");
        availableBalance = SAJsonParser.getInt(jsonObject, "availableBalance");
        pending = SAJsonParser.getInt(jsonObject, "pending");
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "totalReceived", totalReceived,
                "total", total,
                "totalPointsReceivedInCurrentApp", totalPointsReceivedInCurrentApp,
                "availableBalance", availableBalance,
                "pending", pending
        });
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public int getTotalReceived() {
        return totalReceived;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalPointsReceivedInCurrentApp() {
        return totalPointsReceivedInCurrentApp;
    }

    public int getAvailableBalance() {
        return availableBalance;
    }

    public int getPending() {
        return pending;
    }


}
