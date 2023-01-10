package com.tarento.idm.connector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetAuthenticationDetails {
    final static String filePath
            = "/home/jini/IDM/config";

    public static void getHttpRequestDetails() throws IOException {
        Map<String, String> mapFromFile
                = ReadFile();
        //reading from the keys in the text file
        MainConnector.HTTP_PASSWORD= mapFromFile.get("HTTP_PASSWORD");
        MainConnector.USER_NAME= mapFromFile.get("USER_NAME");
        MainConnector.HTTP_BASE_URL=mapFromFile.get("HTTP_BASE_URL");
        MainConnector.HTTP_ENDPOINT=mapFromFile.get("HTTP_ENDPOINT");
        MainConnector.AUTHENTICATION_TYPE=mapFromFile.get("AUTHENTICATION_TYPE");
        MainConnector.CONTENT_TYPE=mapFromFile.get("CONTENT_TYPE");
        MainConnector.ACCEPT_CONTENT_TYPE=mapFromFile.get("ACCEPT_CONTENT_TYPE");
        MainConnector.HTTP_METHOD=mapFromFile.get("HTTP_METHOD");

        //query=mapFromFile.get("query");
}
    public static Map<String,String>ReadFile()
    {
        Map<String, String> map
                = new HashMap<String, String>();
        BufferedReader br = null;

        try {
            // create file object
            File file = new File(filePath);
            // create BufferedReader object from the File
            br = new BufferedReader(new FileReader(file));
            String line = null;
            // read file line by line
            while ((line = br.readLine()) != null) {
                // split the line by :
                String[] parts = line.split(">");
                String key = parts[0].trim();
                String value = parts[1].trim();
                // put key value pair to HashMap if they are not empty
                if (!key.equals("") && !value.equals(""))
                    map.put(key,value);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

            // Always close the BufferedReader
            if (br != null) {
                try {
                    br.close();
                }
                catch (Exception e) {
                };
            }
        }
        return map;
    }

//    public static String baseURI = "https://tarento.freshservice.com/api/v2/";
//
//
//    public static final String endpoint="tickets/filter?query=";
//
//
//    public static final String query="\"group_id:27000745030 AND status:2\"";
//
//
//    public static final String username="GxNCBm6flOIgnujdqvT";


//    public static final String password="X";


}
