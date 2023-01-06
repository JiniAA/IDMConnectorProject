package com.tarento.idm.connector;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Set;

public class JsonResponseHandler extends HttpRequestCall {
    public static String FirstKey;
    public static String f=null;
    public static int flag;
    public static Set<String> keys ;

    public static void handleJsonResponse () {
        try {
            Integer statusCode = response.getStatusLine().getStatusCode();
            System.out.println(statusCode);
            InputStream httpResponse= response.getEntity().getContent();

            JsonParser jsonParser =new JsonParser();
//            JsonObject jsonObject= (JsonObject) jsonParser
//                    .parse(new InputStreamReader(httpResponse,"UTF-8"));

            String jsonString= String.valueOf(jsonParser
                    .parse(new InputStreamReader(httpResponse,"UTF-8")));
            Character firstchar=jsonString.charAt(0);
            System.out.println(firstchar);
            if(firstchar=='{')
            {
                Gson g = new Gson();
                JsonObject jsonObject=g.fromJson(jsonString,JsonObject.class);
                handleJSONObject(jsonObject,f);
                //taking keys of the Jsonobject
                keys = jsonObject.keySet();
                //taking First key of the json response
                FirstKey = keys.iterator().next();
                System.out.println(FirstKey);
            }
            else{
                Gson g = new Gson();
                JsonArray jsonArrayResponse=g.fromJson(jsonString,JsonArray.class);

                handleJSONArray(jsonArrayResponse,f);
            }

            //calling flattenJsonObject Function to iterate through the json response
           // handleJSONObject(jsonObject,f);
            System.out.println("DSEentry"+entry);
            System.out.println(entry.size());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void handleJSONObject(JsonObject jsonObject1,String firstKey){
        Set<String> keyss = jsonObject1.keySet();
        for (String key : keyss) {
            String entryKey =key;
            Object value = jsonObject1.get(key);
            //System.out.println("value "+value);
            if (value instanceof JsonObject) {
                flag=0;
                System.out.println("flag "+flag);
                System.out.println(((JsonObject) value).keySet());
                //checking whether Json object is empty or not
                if(((JsonObject) value).size()==0)
                    entry.put(entryKey, value.toString());
                else
                    //if json object not empty
                    handleJSONObject((JsonObject) value, entryKey);
                continue;
            }
            if (value instanceof JsonArray) {
                if(((JsonArray) value).size()==0)
                    entry.put(entryKey, value.toString());
                else
                    handleJSONArray((JsonArray) value,key);
                continue;
            }
            entry.put(entryKey, value.toString());
        }
    }

    public static void handleJSONArray(JsonArray jsonArray,String firstKeys){
            for (int i = 0; i < jsonArray.size(); i++) {
            String entryKey =String.valueOf(i);
            Object object = jsonArray.get(i);
            if (object instanceof JsonObject) {
                handleJSONObject((JsonObject) object, entryKey);
            } else if (object instanceof JsonArray) {
                handleJSONArray((JsonArray) object, entryKey);
            }
            //entry.put(entryKey, object.toString());
        }
    }
}
