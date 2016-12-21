package kws.superawesome.tv.kwsparentsdk.services;

import org.json.JSONObject;

interface KWSServiceInterface {
    String getEndpoint ();
    KWSHTTPMethod getMethod ();
    JSONObject getQuery ();
    JSONObject getHeader ();
    JSONObject getBody ();
    boolean needsLoggedUser ();
    void success(int status, String payload, boolean success);
}
