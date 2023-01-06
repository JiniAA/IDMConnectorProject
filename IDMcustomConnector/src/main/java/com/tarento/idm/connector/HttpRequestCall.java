package com.tarento.idm.connector;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.HostnameVerifier;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;


public class HttpRequestCall extends GetAuthenticationDetails {
//    private static HttpResponse response;
    private static final String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }
    // Get method
    public static void httpGetRequest(String baseURI,String endpoint, CloseableHttpClient httpClient) {
        try {
            HttpGet httpget = new HttpGet();
            //httpget.setURI(new URI(baseURI+endpoint+ URLEncoder.encode(query, StandardCharsets.UTF_8)));
            httpget.setURI(new URI(baseURI + endpoint));
            httpget.addHeader("Authorization", getBasicAuthenticationHeader(USER_NAME, HTTP_PASSWORD));
            HttpResponse httpResponse = httpClient.execute(httpget);
            if (httpResponse != null) {
                response = httpResponse;
            }

        } catch (Exception e) {
            e.getMessage();
        }

    }
    public static HttpResponse getResponse() {
        return response;
    }


    // Client for SSL
    public static CloseableHttpClient getSSLCustomClient() {
        HttpClientBuilder clientBuilder = HttpClients.custom();
        clientBuilder.setSSLSocketFactory(getSSLContext());
        CloseableHttpClient client = clientBuilder.build();
        return client;
    }

    // Client for multi thread
    public static CloseableHttpClient getConcurrentClient(int threadPoolCount) {
        // Create the pool connection manager
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // Set the pool size
        connectionManager.setMaxTotal(threadPoolCount);

        // Make the client builder
        HttpClientBuilder clientBuilder = HttpClients.custom();
        // Set the connection manager
        clientBuilder.setConnectionManager(connectionManager);
        // Build the client
        CloseableHttpClient client = clientBuilder.build();
        return client;
    }
    // Default client
    public static CloseableHttpClient getDefaultClient() {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        return closeableHttpClient;
    }
    // In case your service is secure with SSL and Certs
    private static SSLConnectionSocketFactory getSSLContext() {

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
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connFactory;
    }
}
