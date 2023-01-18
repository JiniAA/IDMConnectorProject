package se.tarento.idm.generic;

import com.google.gson.*;
import com.jayway.jsonpath.JsonPath;
import com.sap.idm.ic.DSEEntry;
import com.sap.idm.ic.FromCustom;

import com.sap.idm.ic.sfsf.json.DeferredJSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.swing.text.html.parser.DTD;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.simple.JSONValue;


public class FromCustomSystem extends FromCustom {
    CloseableHttpClient httpClient;
    public static String SYSTEM_BASEURL ="https://world.openfoodfacts.org/api/v0/";
    public static String SYSTEM_ENDPOINT ="product/737628064502.json";
    //helpdesk/tickets/view/587296?format=json

    public static String SYSTEM_USERNAME ="";
    public static String SYSTEM_PASSWORD ="";
    public static String REQUEST_HTTPMETHOD ="GET";
    public static String REQUEST_CONTENTTYPE ="UTF-8";
    public static String SYSTEM_AUTHENTICATIONTYPE ="Basic ";
    public static String ACCEPT_CONTENT_TYPE ="application/json";
   // public static DSEEntry dseentry = new DSEEntry("DSEENTRY");
    static String REQUEST_AuthorizationEncoding;
    public static HttpResponse response;
    public static String input="$.product";
    private Iterator<DSEEntry> entriesIterator;
    public void getRequestDetails(){
//        String SYSTEM_BaseURL = getPassAttr("SYSTEM_BASEURL");
//        String SYSTEM_UserName = getPassAttr("SYSTEM_ENDPOINT");
//        String SYSTEM_Password = getPassAttr("SYSTEM_PASSWORD");
//        String REQUEST_HttpMethod = getPassAttr("REQUEST_HTTPMETHOD");
//        String REQUEST_ContentType = getPassAttr("REQUEST_CONTENTTYPE");
//        String SYSTEM_AUTHENTICATIONTYPE = getPassAttr("SYSTEM_AUTHENTICATIONTYPE");
//        String ACCEPT_CONTENT_TYPE = getPassAttr("ACCEPT_CONTENT_TYPE");
    }
    @Override
    public boolean initCustom() {
        try {
            logIt(3, "try block Started: initCustom");
            REQUEST_AuthorizationEncoding=getBasicAuthenticationEncoding(SYSTEM_AUTHENTICATIONTYPE,SYSTEM_USERNAME,SYSTEM_PASSWORD );
            String RESPONSE=doExecuteHttpRequest(REQUEST_HTTPMETHOD,REQUEST_AuthorizationEncoding,REQUEST_CONTENTTYPE);
            if (RESPONSE.isEmpty()) {
               // logIt(2, "Method ended: FromCustomSystem.initCustom.doExecuteHttpRequest with result: ";
                return false;
            }
            List<DSEEntry> entries = parseHttpRequestResponse(RESPONSE);
            System.out.println(entries.size());
            this.entriesIterator = entries.iterator();
            return true;
        } catch (Exception e) {
            logIt(1, "initCustom Method Exception Error: ", e);
            return false;
        }

    }

    @Override
    public DSEEntry getNextEntry() {
        return this.entriesIterator.next();
    }

    @Override
    public boolean hasMore() {
           if (this.entriesIterator.hasNext()) {
               return true;
           } else
               return false;
    }
    @Override
    public void exit() {
        try {
            getSSLCustomClient().close();
            logIt(3, "connection closed");
        } catch (IOException e) {
            logIt(1, "connection closed error"+e);

        }

    }
    private String getBasicAuthenticationEncoding(String AutheticationType, String username, String password) {
        logIt(3, "Method Started: getBasicAuthenticationEncoding.initCustom");
        String valueToEncode=null;
        String encodedString=null;
        if (AutheticationType.equalsIgnoreCase("Basic ")) {
            valueToEncode = username + ":" + password;
            encodedString="Basic" + " "  + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
        }
        logIt(3, "encodedString: " + encodedString + "getAuthorizaitonEncoding.initCustom");
        return encodedString;
    }

    private String  doExecuteHttpRequest(String REQUEST_HTTPMETHOD,String REQUEST_AuthorizationEncoding,String REQUEST_CONTENTTYPE){
        logIt(3, "Method Started: doExecuteHttpRequest.initCustom");
        String RESULT=null;
        try{
            if(REQUEST_HTTPMETHOD.equalsIgnoreCase("GET")) {
               RESULT= httpGetRequest(SYSTEM_BASEURL, SYSTEM_ENDPOINT,REQUEST_AuthorizationEncoding, getSSLCustomClient());
            }
          return RESULT;

        } catch (Exception e) {
            logIt(1, "RuntimeException"+e);
            throw new RuntimeException(e);
        }

    }
    public String httpGetRequest(String baseURI,String endpoint, String REQUEST_AuthorizationEncoding,CloseableHttpClient httpClient) {
        String result=null;
        try {
            HttpGet httpget = new HttpGet();
            //httpget.setURI(new URI(baseURI+endpoint+ URLEncoder.encode(query, StandardCharsets.UTF_8)));
            httpget.setURI(new URI(baseURI + endpoint));
            httpget.addHeader("Authorization", REQUEST_AuthorizationEncoding);
            httpget.addHeader("Accept",ACCEPT_CONTENT_TYPE);
            HttpResponse httpResponse = httpClient.execute(httpget);
            if (httpResponse != null) {
                response = httpResponse;
                HttpEntity entity = response.getEntity();
                if (entity != null)
                    result = EntityUtils.toString(entity);
            } else {
                logIt(1, "Error : doExecuteHttpRequest.initCustom Null Response " + response.getStatusLine().toString());
               // throw new Exception(response.getStatusLine().toString());
            }
            logIt(3, "result: doExecuteHttpRequest.initCustom " + result);
//            return result;
        } catch (URISyntaxException ex) {
            logIt(1, "RuntimeException"+ex);
        } catch (ClientProtocolException ex) {
            logIt(1, "RuntimeException"+ex);
        } catch (IOException ex) {
            logIt(1, "RuntimeException"+ex);
        } catch (Exception ex) {
            logIt(1, "RuntimeException"+ex);
        }
        return result;
    }
    public List parseHttpRequestResponse(String Response) {
        logIt(3, "parseHttpRequestResponse:  ");
        List<DSEEntry> entries = new ArrayList<DSEEntry>();
        try {
            Object QueryValue = JsonPath.read(Response, input, new com.jayway.jsonpath.Predicate[0]);
            if (QueryValue.getClass().getName().toString().indexOf("JSONArray") > 0) {
            //if(QueryValue instanceof JSONArray)
            //{
                logIt(3, QueryValue.getClass().getName());
                JSONArray jsonArray = new JSONArray(QueryValue.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    logIt(3, "For Loop JSONArray Counter: i  " + i);
                    JSONObject jsonObjectVal = jsonArray.getJSONObject(i);
                    logIt(3, "Dse Entry Object Created for " + jsonObjectVal);
                    DSEEntry dseentry = new DSEEntry();
                    dseentry.setName("DSEEntry");
                    handleJSONObject(dseentry, jsonObjectVal, (String) null);
                    entries.add(dseentry);
                }
            }
            else {
                GsonBuilder builder = new GsonBuilder();
                builder.serializeNulls();
                Gson gson = builder.setPrettyPrinting().create();
                String json = gson.toJson(QueryValue, LinkedHashMap.class);
                Object jsonValue = new JSONTokener(json).nextValue();
                DSEEntry dseentry = new DSEEntry();
                if (jsonValue instanceof JSONObject) {
                    dseentry.setName("DSEEntry");
                    handleJSONObject(dseentry, (JSONObject) jsonValue, (String) null);
                    entries.add(dseentry);
                }
            }

            System.out.println("entries"+entries);
            //return entries;

        } catch (RuntimeException e) {
            logIt(1, "parseHttpRequestResponse:  "+e);

        }
        return entries;
    }

    public  void handleJSONObject(DSEEntry dseentry,JSONObject jsonObject1, String parentKey){
        logIt(3, "Method Started: handleJSONObject.initCustom");
        Set<String> keyss = jsonObject1.keySet();
        for (String key : keyss) {
            String entryKey = (parentKey == null) ? key : (String.valueOf(String.valueOf(parentKey)) + '.' + key);
            //String entryKey =key;
            Object value = jsonObject1.get(key);
            if (value instanceof JSONObject) {
                //checking whether Json object is empty or not
                if(((JSONObject) value).length()==0) {
                    dseentry.put(entryKey, value.toString());
                }
                else if((JSONObject) value==null)
                    dseentry.put(entryKey, value);
                else
                    //if json object not empty
                    handleJSONObject(dseentry,(JSONObject) value, entryKey);
                continue;
            }
            if (value instanceof JSONArray) {
                if(((JSONArray) value).length()==0) {
                    dseentry.put(entryKey, value.toString());
                }
                else
                    handleJSONArray(dseentry,(JSONArray)value,entryKey);
                continue;
            }
            dseentry.put(entryKey, value.toString());

        }
    }

    public void handleJSONArray(DSEEntry dseentry,JSONArray jsonArray,String parentKey){
        logIt(3, "Method Started: handleJSONArray.initCustom");
        for (int i = 0; i < jsonArray.length(); i++) {
            String entryKey = (parentKey == null) ? "" : (String.valueOf(String.valueOf(parentKey)) + "[" + i + "]");
            //String entryKey =String.valueOf(i);
            Object object = jsonArray.get(i);
            if (object instanceof JsonObject) {
                handleJSONObject(dseentry,(JSONObject) object, entryKey);
            } else if (object instanceof JSONArray) {
                handleJSONArray(dseentry,(JSONArray) object, entryKey);
            }
            dseentry.put(entryKey, object.toString());
        }

    }
    public CloseableHttpClient getSSLCustomClient() {
        HttpClientBuilder clientBuilder = HttpClients.custom();
        clientBuilder.setSSLSocketFactory(getSSLContext());
        CloseableHttpClient client = clientBuilder.build();
        return client;
    }
    private SSLConnectionSocketFactory getSSLContext() {

        TrustStrategy trustStrategy = new TrustStrategy() {

            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                return true;
            }
        };

        HostnameVerifier allVerifier = new NoopHostnameVerifier();
        SSLConnectionSocketFactory connFactory = null;
        try {
            connFactory = new SSLConnectionSocketFactory(
                    SSLContextBuilder.create().loadTrustMaterial(trustStrategy).build(), allVerifier);
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            logIt(1, "KeyManagementException:  "+e);
           // e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            logIt(1, "NoSuchAlgorithmException:  "+e);

           // e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            logIt(1, "KeyStoreException:  "+e);
        }
        return connFactory;
    }
}

