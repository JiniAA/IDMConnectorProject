package com.tarento.idm.connector;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Set;

public class JsonResponseHandler extends HttpRequestCalls {
    public static int flag;
   public static JsonParser jsonParserObj =new JsonParser();
    public static void handleJsonResponse () {
        InputStream httpRes;

        try {
            Integer statusCode = MainConnector.response.getStatusLine().getStatusCode();
            System.out.println(statusCode);
            JsonParser jsonParser =new JsonParser();
            InputStream httpResponse= MainConnector.response.getEntity().getContent();
            String jsonString= String.valueOf(jsonParser
                    .parse(new InputStreamReader(httpResponse,"UTF-8")));
            Object json = new JSONTokener(jsonString).nextValue();
            String f=null;
            Gson g = new Gson();
            if(json instanceof JSONObject)
            {
                JsonObject jsonObject1=g.fromJson(jsonString,JsonObject.class);
                handleJSONObject(jsonObject1,f);
            }
            else {
                JsonArray jsonArrayResponse=g.fromJson(jsonString,JsonArray.class);

                handleJSONArray(jsonArrayResponse,f);
            }
            System.out.println("DSEentry"+MainConnector.entry);
            System.out.println(MainConnector.entry.size());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void JsonObjectParser(InputStream httpRes) throws UnsupportedEncodingException {
        JsonObject jsonObject= (JsonObject) jsonParserObj
                  .parse(new InputStreamReader(httpRes,"UTF-8"));
        System.out.println(jsonObject);

    }

    public static void handleJSONObject(JsonObject jsonObject1,String firstKey){
        Set<String> keyss = jsonObject1.keySet();
        for (String key : keyss) {
            String entryKey =key;
            Object value = jsonObject1.get(key);
            if (value instanceof JsonObject) {
                flag=0;
                //checking whether Json object is empty or not
                if(((JsonObject) value).size()==0)
                    MainConnector.entry.put(entryKey, value.toString());
                else
                    //if json object not empty
                    handleJSONObject((JsonObject) value, entryKey);
                continue;
            }
            if (value instanceof JsonArray) {
                if(((JsonArray) value).size()==0)
                    MainConnector.entry.put(entryKey, value.toString());
                else
                    handleJSONArray((JsonArray) value,key);
                continue;
            }
            MainConnector.entry.put(entryKey, value.toString());
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
