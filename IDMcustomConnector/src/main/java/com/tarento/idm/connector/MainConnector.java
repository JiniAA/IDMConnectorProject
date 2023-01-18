package com.tarento.idm.connector;


import com.sap.idm.ic.DSEEntry;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public class MainConnector {
    public static String HTTP_BASE_URL;
    public static String HTTP_ENDPOINT;
    public static String USER_NAME;
    public static String HTTP_PASSWORD;
    public static String AUTHENTICATION_TYPE;
    public static String CONTENT_TYPE;
    public static String ACCEPT_CONTENT_TYPE;
    public static String HTTP_METHOD;
    public static HttpResponse response;
    public static DSEEntry entry = new DSEEntry("DSEENTRY");


    public static void main(String[] args) throws IOException, URISyntaxException {
        GetAuthenticationDetails.getHttpRequestDetails();
        HttpRequestCalls.httpRequest(HTTP_METHOD);
        JsonResponseHandler.handleJsonResponse();
    }

}