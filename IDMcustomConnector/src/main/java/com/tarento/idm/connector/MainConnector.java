package com.tarento.idm.connector;


import java.io.IOException;
import java.net.URISyntaxException;

public class MainConnector {
    public static void main(String[] args) throws IOException, URISyntaxException {
        GetAuthorizationDetails.readFromFile();
        ResponseHandler.ActionFunction();
    }

}