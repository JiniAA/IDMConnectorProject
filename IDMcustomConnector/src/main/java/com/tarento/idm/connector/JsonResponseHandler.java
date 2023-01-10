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

public class JsonResponseHandler extends HttpRequestCall {
    public static String FirstKey;
    public static String f=null;
    public static int flag;
    public static Set<String> keys ;
    public static JsonParser jsonParser =new JsonParser();
   public static JsonParser jsonParserObj =new JsonParser();
    public static JsonParser jsonParserArr =new JsonParser();
    public static InputStream httpResponse;
    public static JsonObject jsonObject;
   // private static InputStream httpRes;


    public static void handleJsonResponse () {
        InputStream httpRes;

        try {
            Integer statusCode = MainConnector.response.getStatusLine().getStatusCode();
            System.out.println(statusCode);

//            InputStream httpResponse= MainConnector.response.getEntity().getContent();
//            InputStream httpRes =httpResponse;

              httpResponse= MainConnector.response.getEntity().getContent();
              httpRes =MainConnector.response.getEntity().getContent();


//            JsonParser jsonParser =new JsonParser();
//            JsonParser jsonParserObj =new JsonParser();
//            JsonParser jsonParserArr =new JsonParser();
//            JsonObject jsonObject= (JsonObject) jsonParserObj
//                  .parse(new InputStreamReader(httpResponse,"UTF-8"));
//            System.out.println(jsonObject);


            String jsonString= String.valueOf(jsonParser
                    .parse(new InputStreamReader(httpResponse,"UTF-8")));
            Object json = new JSONTokener(jsonString).nextValue();

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

//            JsonArray jsonArray1= (JsonArray) jsonParserArr
//                    .parse(new InputStreamReader(httpResponse,"UTF-8"));
//            System.out.println(jsonArray1);

//            JsonParser jsonParser = new JsonParser().parse(new InputStreamReader(httpResponse,"UTF-8"));

//            String type= jsonParser.parse(new InputStreamReader(httpResponse,"UTF-8")).getClass().getName();
//            System.out.println(type);
//            String j="com.google.gson.JsonObject";
//            String Ar="com.google.gson.JsonArray";
//
//            if(type.equals(j))
//            {
//                JsonResponseHandler.JsonObjectParser(httpRes);
//                //handleJSONObject(JsonObjectParser(httpRes),f);
//            }
//            else{
//                JsonArray jsonArray = (JsonArray) jsonParser
//                        .parse(new InputStreamReader(httpRes, "UTF-8"));
//                handleJSONArray(jsonArray, f);
//            }


//            Character firstchar=jsonString.charAt(0);
//            System.out.println(firstchar);
//            if(firstchar=='{')
//            {
//                Gson g = new Gson();
//                JsonObject jsonObject1=g.fromJson(jsonString,JsonObject.class);
//                handleJSONObject(jsonObject1,f);
//                //taking keys of the Jsonobject
//                keys = jsonObject1.keySet();
//                //taking First key of the json response
//                FirstKey = keys.iterator().next();
//                System.out.println(FirstKey);
//            }
//            else{
//                Gson g = new Gson();
//                JsonArray jsonArrayResponse=g.fromJson(jsonString,JsonArray.class);
//
//                handleJSONArray(jsonArrayResponse,f);
//            }
//            if(jsonParser.parse(new InputStreamReader(httpResponse,"UTF-8")) instanceof JsonObject)
//            {
//                System.out.println("ji");
//            }

            //calling flattenJsonObject Function to iterate through the json response
           // handleJSONObject(jsonObject,f);
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
            //System.out.println("value "+value);
            if (value instanceof JsonObject) {
                flag=0;
                System.out.println("flag "+flag);
                System.out.println(((JsonObject) value).keySet());
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
