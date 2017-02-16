package kws.superawesome.tv.kwsparentdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import kws.superawesome.tv.kwsparentsdk.KWSParent;
import kws.superawesome.tv.kwsparentsdk.models.parent.KWSParentUser;
import kws.superawesome.tv.kwsparentsdk.services.create.KWSParentCreateUserInterface;
import kws.superawesome.tv.kwsparentsdk.services.create.KWSParentCreateUserStatus;
import kws.superawesome.tv.kwsparentsdk.services.oauth.KWSParentLoginUserInterface;
import kws.superawesome.tv.kwsparentsdk.services.parent.KWSParentGetUserInterface;
import kws.superawesome.tv.kwsparentsdk.services.parent.KWSParentUpdateUserInterface;

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

        KWSParent.sdk.loginUser(this, email, password, new KWSParentLoginUserInterface() {
            @Override
            public void didLoginUser(boolean operationOK) {
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
        KWSParent.sdk.logoutUser(this);
        logs += "Logged out of this user\n";
        textView.setText(logs);
        authButton.setText("Login as KWS Parent");
    }

    public void getParentData (View view) {
        KWSParent.sdk.getUser(this, new KWSParentGetUserInterface() {
            @Override
            public void didGetUser(KWSParentUser parent) {
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

        KWSParent.sdk.updateUser(this, updated, new KWSParentUpdateUserInterface() {
            @Override
            public void didUpdateUser(boolean operationOK) {
                if (operationOK) {
                    logs += "Updated user successfully!\n";
                } else {
                    logs += "Failed to updateUser user data\n";
                }
                textView.setText(logs);
            }
        });
    }

    public void getChildren (View view) {
        KWSParent.sdk.createUser(this, "gabriel.coman+1013@superawesome.tv", "tetestttasa", new KWSParentCreateUserInterface() {
            @Override
            public void didCreateUser(KWSParentCreateUserStatus status) {
                Log.d("App", "Result is " + status);
            }
        });
    }
}
