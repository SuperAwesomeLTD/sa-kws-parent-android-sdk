package kws.superawesome.tv.kwsparentsdk.services.oauth;

import kws.superawesome.tv.kwsparentsdk.models.oauth.KWSLoggedUser;

public interface KWSInternalAuthInterface {
    void didAuthUser (KWSLoggedUser user);
}
