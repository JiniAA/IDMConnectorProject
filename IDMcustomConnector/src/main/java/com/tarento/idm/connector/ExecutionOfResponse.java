package com.tarento.idm.connector;

import com.google.gson.*;
import com.sap.idm.ic.DSEEntry;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class ExecutionOfResponse extends HttpCallsInitialization{
    public static void ActionFunction() {
        try {
            HttpCallsInitialization.GET(baseURI,endpoint,query, HttpCallsInitialization.getSSLCustomClient());
            HttpResponse response = HttpCallsInitialization.getResponse();


            InputStream httpResponse= response.getEntity().getContent();

            JsonParser jsonParser =new JsonParser();
            JsonObject jsonObject= (JsonObject) jsonParser
                    .parse(new InputStreamReader(httpResponse,"UTF-8"));

            JsonArray element=jsonObject.getAsJsonArray("tickets");

            ArrayList arrayList =new ArrayList();
            for (Integer j = 0; j < element.size(); j++) {
                arrayList.add(element.get(j));
            }

            Iterator It= arrayList.listIterator();

            try {
                while (It.hasNext()){
                    JsonObject jsonObj ;
                    jsonObj = (JsonObject) It.next();
                    HashMap<String,String> entry2= new Gson().fromJson(jsonObj.toString(), DSEEntry.class);
                    entry= (DSEEntry) entry2;

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


//            JsonObject jsonObject1 = (JsonObject) element.get(0);
//            HashMap<String,String> entry1= new Gson().fromJson(jsonObject1.toString(), DSEEntry.class);
//            //            DSEEntry entry;
//            entry= (DSEEntry) entry1;

            System.out.println(entry);
            System.out.println(entry.keySet());
            System.out.println(entry.values());
            System.out.println(entry.get("id"));

            //return entry;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
