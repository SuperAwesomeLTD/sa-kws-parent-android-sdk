package kws.superawesome.tv.kwsparentdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import kws.superawesome.tv.kwsparentsdk.KWSParent;
import kws.superawesome.tv.kwsparentsdk.aux.KWSLogger;
import kws.superawesome.tv.kwsparentsdk.models.oauth.KWSLoggedUser;
import kws.superawesome.tv.kwsparentsdk.models.parent.KWSParentUser;
import kws.superawesome.tv.kwsparentsdk.services.create.KWSCreateParentInterface;
import kws.superawesome.tv.kwsparentsdk.services.create.KWSCreateParentStatus;
import kws.superawesome.tv.kwsparentsdk.services.oauth.KWSAuthInterface;
import kws.superawesome.tv.kwsparentsdk.services.oauth.KWSInternalAuthInterface;
import kws.superawesome.tv.kwsparentsdk.services.parent.KWSGetParentInterface;
import kws.superawesome.tv.kwsparentsdk.services.parent.KWSUpdateParentInterface;

public class MainActivity extends AppCompatActivity {

    private String logs = "";
    private TextView textView;
    private Button authButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KWSParent.sdk.setup(this);

        textView = (TextView) findViewById(R.id.LogView);
        authButton = (Button) findViewById(R.id.LoginLogoutButton);

        if (KWSParent.sdk.isUserLogged()) {
            logs += "Already found user " + KWSParent.sdk.getLoggedUser().getMetadata().getUserId() + "\n";
            authButton.setText("Logged in with " + KWSParent.sdk.getLoggedUser().getMetadata().getUserId());
        } else {
            authButton.setText("Login as KWS Parent");
        }
    }

    public void authUser (View view) {
        if (KWSParent.sdk.isUserLogged()) {
            logoutUser(view);
        } else {
            loginUser(view);
        }
    }

    public void loginUser (View view) {
        final String email = "gabriel.coman+1013@superawesome.tv";
        final String password = "testtest";

        KWSParent.sdk.login(this, email, password, new KWSAuthInterface() {
            @Override
            public void didAuthUser(boolean operationOK) {
                if (operationOK) {
                    logs += "Managed to authenticate user " + email + "\n";
                } else {
                    logs += "Did not manage to authenticate user " + email + "\n";
                }
                textView.setText(logs);
            }
        });
    }

    public void logoutUser (View view) {
        KWSParent.sdk.logout(this);
        logs += "Logged out of this user\n";
        textView.setText(logs);
        authButton.setText("Login as KWS Parent");
    }

    public void getParentData (View view) {
        KWSParent.sdk.getParentData(this, new KWSGetParentInterface() {
            @Override
            public void gotParent(KWSParentUser parent) {
                if (parent != null) {
                    logs += "Parent data is " + parent.writeToJson().toString() + "\n";
                } else {
                    logs += "Could not get parent data\n";
                }
                textView.setText(logs);
            }
        });
    }

    public void updateParentData (View view) {
        KWSParentUser updated = new KWSParentUser(new JSONObject());
        updated.setFirstName("Juan");
        updated.setLastName("Marcos");

        KWSParent.sdk.updateParentData(this, updated, new KWSUpdateParentInterface() {
            @Override
            public void updatedParent(boolean operationOK) {
                if (operationOK) {
                    logs += "Updated user successfully!\n";
                } else {
                    logs += "Failed to update user data\n";
                }
                textView.setText(logs);
            }
        });
    }

    public void getChildren (View view) {
        KWSParent.sdk.create(this, "gabriel.coman+1013@superawesome.tv", "tetestttasa", new KWSCreateParentInterface() {
            @Override
            public void didCreateParent(KWSCreateParentStatus status) {
                KWSLogger.log("App", "Result is " + status);
            }
        });
    }
}
