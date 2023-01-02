package com.tarento.idm.connector;

import com.sap.idm.ic.DSEEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConstantsUsed {
    public static String us = null;
    public static String ps = null;
    public static String baseURI =null;
    public static String endpoint =null;
    public static String query =null;



    final static String filePath
            = "/home/jini/IDM/configg";

    public static void readFromFile() throws IOException {
        Map<String, String> mapFromFile
                = HashMapFromTextFile();
        ps= mapFromFile.get("pass");
        us= mapFromFile.get("user");
        baseURI=mapFromFile.get("baseURL");
        endpoint=mapFromFile.get("end");
        query=mapFromFile.get("query");

        System.out.println(ps);
        System.out.println(us);

}
    public static Map<String,String>HashMapFromTextFile()
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
                String[] parts = line.split("-");
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
    public static DSEEntry entry = new DSEEntry("DSEENTRY");

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



    public static final String enc="Basic";
    public static final String encodingType="Base64";

    //public static DSEEntry<String,String> entry1= new Gson().fromJson(jsonObject1.toString(),DSEEntry.class);




}
