package com.tarento.idm.connector;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Set;

public class ResponseHandler extends HttpCallsInitialization{
    public static String FirstKey;
    public static String f=null;

    public static int flag;

    public static Set<String> keys ;

    public static void ActionFunction3() {
        try {
            HttpCallsInitialization.GET(baseURI,endpoint,query, HttpCallsInitialization.getSSLCustomClient());
            HttpResponse response = HttpCallsInitialization.getResponse();
            Integer statusCode = response.getStatusLine().getStatusCode();
            System.out.println(statusCode);

            InputStream httpResponse= response.getEntity().getContent();

            JsonParser jsonParser =new JsonParser();
            JsonObject jsonObject= (JsonObject) jsonParser
                    .parse(new InputStreamReader(httpResponse,"UTF-8"));
            //taking keys of the Jsonobject
            keys = jsonObject.keySet();
            //taking First key of the json response
            FirstKey = keys.iterator().next();
            System.out.println(FirstKey);

            //calling flattenJsonObject Function to iterate through the json response
            flattenJSONObject(jsonObject,f);
            System.out.println("DSEentry"+entry);
            System.out.println(entry.size());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void flattenJSONObject(JsonObject jsonObject1,String firstKey){
        Set<String> keyss = jsonObject1.keySet();
        for (String key : keyss) {
            //String entryKey = (firstKey == null) ? key : (String.valueOf(firstKey) + '.' + key);
            String entryKey =key;
//            System.out.println("entry key "+entryKey);
            //System.out.println("key "+key+"\ngetkey "+jsonObject1.get(key));
            Object value = jsonObject1.get(key);
            //System.out.println("value "+value);
            if (value instanceof JsonObject) {
                flag=0;
                System.out.println("flag "+flag);
                System.out.println(((JsonObject) value).keySet());

                flattenJSONObject((JsonObject) value, entryKey);
                continue;
            }
            if (value instanceof JsonArray) {
                if(value.equals("[]"))
                    entry.put(entryKey, value.toString());
                //if(((JsonArray) value).isJsonObject()) {
                    //flattenJSONArray(((JsonArray) value).getAsJsonObject().getAsJsonArray(), key);
                else
                flattenJSONArray((JsonArray) value,key);
               // }
//                else
//                    entry.put(entryKey, value.toString());

                continue;
            }
            entry.put(entryKey, value.toString());

        }


    }

    public static void flattenJSONArray(JsonArray jsonArray,String firstKeys){
            for (int i = 0; i < jsonArray.size(); i++) {
            String entryKey = (firstKeys == null) ? "" : (String.valueOf(firstKeys) + "[" + i + "]");
            //String entryKey = firstKeys;
            Object object = jsonArray.get(i);
            if (object instanceof JsonObject) {
                flattenJSONObject((JsonObject) object, entryKey);
            } else if (object instanceof JsonArray) {
                flattenJSONArray((JsonArray) object, entryKey);
            }
            entry.put(entryKey, object.toString());
        }

    }


}
