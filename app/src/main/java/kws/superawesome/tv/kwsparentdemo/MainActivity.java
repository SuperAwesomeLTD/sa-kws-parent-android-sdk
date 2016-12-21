package kws.superawesome.tv.kwsparentdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kws.superawesome.tv.kwsparentsdk.KWSParent;
import kws.superawesome.tv.kwsparentsdk.aux.KWSLogger;
import kws.superawesome.tv.kwsparentsdk.models.oauth.KWSLoggedUser;
import kws.superawesome.tv.kwsparentsdk.services.oauth.KWSAuthService;
import kws.superawesome.tv.kwsparentsdk.services.oauth.KWSAuthServiceInterface;
import tv.superawesome.lib.sautils.SAUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KWSParent.sdk.setup(this);
        KWSParent.sdk.login(this, "gabriel.coman+5@superawesome.tv", "testtest", new KWSAuthServiceInterface() {
            @Override
            public void didAuthUser(KWSLoggedUser user) {

            }
        });
    }
}
