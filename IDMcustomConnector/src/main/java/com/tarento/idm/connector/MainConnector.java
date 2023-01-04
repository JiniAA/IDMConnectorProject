package com.tarento.idm.connector;


import java.io.IOException;
import java.net.URISyntaxException;

public class MainConnector {
    public static void main(String[] args) throws IOException, URISyntaxException {
        ConstantsUsed.readFromFile();
        //ResponseHandler.ActionFunction2();
        ResponseHandler.ActionFunction3();

        //ExecutionOfResponse.ActionFunction();

    }

}