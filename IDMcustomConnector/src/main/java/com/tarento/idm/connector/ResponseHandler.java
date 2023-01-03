package com.tarento.idm.connector;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class ResponseHandler extends ExecutionOfResponse{
    public static void ActionFunction2() {
        try {
            HttpCallsInitialization.GET(baseURI,endpoint,query, HttpCallsInitialization.getSSLCustomClient());
            HttpResponse response = HttpCallsInitialization.getResponse();
            Integer statusCode = response.getStatusLine().getStatusCode();
            System.out.println(statusCode);

            InputStream httpResponse= response.getEntity().getContent();

            JsonParser jsonParser =new JsonParser();
            JsonObject jsonObject= (JsonObject) jsonParser
                    .parse(new InputStreamReader(httpResponse,"UTF-8"));


            //iterator through keys of the jsonObject
            Set<String> keys = jsonObject.keySet();
            Iterator i = keys.iterator();

            ArrayList arrayList2 =new ArrayList();
            while (i.hasNext()) {
                //keys in the jsonObject
                System.out.println("keys="+ i.next());
            }
            //values of the keys
           // Collection getValues = jsonObject.entrySet();
           // Set<Map.Entry<String, JsonElement>> getValues = jsonObject.entrySet();
            Collection getValues = jsonObject.entrySet().stream().map(stringJsonElementEntry -> stringJsonElementEntry.getValue()).collect(Collectors.toList());
            System.out.println("full values"+getValues);
           Iterator ii = getValues.iterator();
            while (ii.hasNext()) {
//                System.out.println(i.next().toString());
//                System.out.println(getValues.size());
                JsonArray el=jsonObject.getAsJsonArray(ii.next().toString());
                System.out.println("el"+el);


            }


//            Iterator iter = arrayList2.iterator();
//
//            try {
//                while (iter.hasNext()){
//                    JsonArray li ;
//                    li = (JsonArray) iter.next();
//                    System.out.println("iter.next="+iter.next());
//                    HashMap<String,String> entry2= new Gson().fromJson(li.toString(), DSEEntry.class);
//                    entry= (DSEEntry) entry2;
//
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
            //return entry;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
