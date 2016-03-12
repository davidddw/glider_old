package com.yunshan.cloudbuilder;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

public class RESTClient {
    protected static final Logger s_logger = Logger.getLogger(RESTClient.class);

    private final static String VERSION = "v1";
    private final static String USERNAME = "admin";
    private final static String PASSWORD = "admin1";
    private final static String APPKEY = "5ee218a0-25ec-463a-81f5-6c5364707cda";
    private final static String CONTENTTYPE = "application/json; charset=utf-8";
    private final static int CONST_TIME_OUT = 6000;

    private String host;

    private HttpResponse response = null;
    private HashMap<String, String> headers = null;

    private void setHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put(key, value);
    }

    public RESTClient(String host) {
        this.host = host;
        setHeader("Content-Type", CONTENTTYPE);
        setHeader("APPKEY", APPKEY);
        String str = USERNAME + ":" + PASSWORD;
        final byte[] bytes = str.getBytes();
        setHeader("Authorization", "Basic " + new String(Base64.encodeBase64(bytes)));
    }

    private static HttpClient acceptsUntrustedCerts() {
        SSLContext trustStrategy;
        try {
            trustStrategy = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                    trustStrategy, new NoopHostnameVerifier());
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslSocketFactory).build();
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                    socketFactoryRegistry);
            CloseableHttpClient client = HttpClients.custom().setSSLContext(trustStrategy)
                    .setConnectionManager(connectionManager).build();
            return client;
        } catch (Exception e) {
            s_logger.error("exception: ", e);
        }
        return null;
    }

    private static String generateURL(String host, String cmd, String param, boolean isApp) {
        String urlSuffix = "";
        cmd = (param != null) ? cmd + "/" + param : cmd;
        String cmd_string = isApp ? cmd : cmd + '/';
        String schema = "";
        if (host.equals("127.0.0.1")) {
            int port = isApp ? 20101 : 20013;
            urlSuffix = ":" + port;
            schema = "http://";
        } else {
            String url_type = isApp ? "appserver" : "talkerserver";
            urlSuffix = "/" + url_type;
            schema = "https://";
        }
        return schema + host + urlSuffix + "/" + VERSION + "/" + cmd_string;

    }

    public ResultSet makeRequest(String method, String uri) {
        return makeRequest(method, uri, null, null);
    }

    protected ResultSet RequestAPP(String method, String cmd, String body, String param) {
        String uri = generateURL(this.host, cmd, param, true);
        return makeRequest(method, uri, body, param);
    }

    protected ResultSet RequestTalker(String method, String cmd, String body, String param) {
        String uri = generateURL(this.host, cmd, param, false);
        return makeRequest(method, uri, body, param);
    }

    protected LinkedList<NameValuePair> newQueryValues(String command,
            Map<String, String> optional) {
        LinkedList<NameValuePair> queryValues = new LinkedList<NameValuePair>();
        queryValues.add(new BasicNameValuePair("command", command));
        if (optional != null) {
            Iterator<Entry<String, String>> optional_it = optional.entrySet().iterator();
            while (optional_it.hasNext()) {
                Map.Entry<String, String> pairs = optional_it.next();
                if (pairs.getValue() != "")
                    queryValues.add(new BasicNameValuePair(pairs.getKey(), pairs.getValue()));
            }
        }
        return queryValues;
    }

    public ResultSet filterRecordsByKey(ResultSet jsonBody, String key, String value) {
        JsonElement elementField = jsonBody.content();
        if (elementField.isJsonArray()) {
            for (JsonElement jsonElement : elementField.getAsJsonArray()) {
                JsonObject jsonNew = jsonElement.getAsJsonObject();
                if (value.equals(jsonNew.get(key).getAsString()))
                    return new ResultSet(jsonNew);
            }
        } else if (elementField.isJsonObject()) {
            JsonObject jsonNew = elementField.getAsJsonObject();
            if (value.equals(jsonNew.get(key).getAsString()))
                return new ResultSet(jsonNew);
        }
        return new ResultSet(200, null);
    }

    public String getStringRecordsByKey(ResultSet jsonBody, String key) {
        JsonElement je = jsonBody.content();
        return (je != null) ? je.getAsJsonObject().get(key).getAsString() : "";
    }

    public int getIntRecordsByKey(ResultSet jsonBody, String key) {
        JsonElement je = jsonBody.content();
        return (je != null) ? je.getAsJsonObject().get(key).getAsInt() : 0;
    }
    
    public JsonObject getJsonObjectRecordsByKey(ResultSet jsonBody, String key) {
        JsonElement je = jsonBody.content();
        return (je != null) ? je.getAsJsonObject().get(key).getAsJsonObject() : null;
    }
    
    public JsonArray getJsonArrayRecordsByKey(ResultSet jsonBody, String key) {
        JsonElement je = jsonBody.content();
        return (je != null) ? je.getAsJsonObject().get(key).getAsJsonArray() : null;
    }

    protected ResultSet simpleResponse(String responseBody) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", responseBody);
        return new ResultSet(jsonObject);
    }

    protected ResultSet handleResponse(int code, String responseBody) {
        ResultSet resultSet = new ResultSet(code);
        Gson gson = new GsonBuilder().create();
        JsonObject jsonBody = gson.fromJson(responseBody, JsonObject.class);
        JsonElement elementField = jsonBody.getAsJsonObject().get("DATA");
        if (elementField != null) {
            if (elementField.isJsonArray()) {
                resultSet.setResultSet(jsonBody.getAsJsonArray("DATA"));
            } else if (elementField.isJsonObject()) {
                resultSet.setResultSet(jsonBody.getAsJsonObject("DATA"));
            }
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("OPT_STATUS",
                    jsonBody.getAsJsonObject().get("OPT_STATUS").getAsString());
            resultSet.setResultSet(jsonObject);
        }
        s_logger.info("Query response: " + resultSet);
        return resultSet;
    }

    protected ResultSet handleErrorResponse(int errorCode, String responseBody) {
        ResultSet resultSet = new ResultSet(errorCode);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", responseBody);
        resultSet.setResultSet(jsonObject);
        s_logger.info("Query response: " + resultSet);
        return resultSet;
    }

    public ResultSet makeRequest(String method, String uri, String data, String param) {
        HttpRequestBase request = null;
        switch (method.toUpperCase()) {
        case "GET":
            request = new HttpGet(uri);
            break;
        case "PUT":
            request = new HttpPut(uri);
            break;
        case "POST":
            request = new HttpPost(uri);
            break;
        case "PATCH":
            request = new HttpPatch(uri);
            break;
        case "DELETE":
            request = new HttpDelete(uri);
            break;
        default:
            s_logger.error("Invalid HTTP request type: " + method);
            return null;
        }
        s_logger.debug("query method is: " + method);

        if (headers != null) {
            String headerStr = "";
            for (Entry<String, String> header : headers.entrySet()) {
                request.setHeader(header.getKey(), header.getValue());
                headerStr = header.getKey() + ":" + header.getValue() + ",";
            }
            s_logger.debug("header: " + headerStr);
        }

        if (data != null) {
            if (request instanceof HttpPut) {
                ((HttpPut) request).setEntity(new StringEntity(data, "UTF-8"));
            }
            if (request instanceof HttpPost) {
                ((HttpPost) request).setEntity(new StringEntity(data, "UTF-8"));
            }
            if (request instanceof HttpPatch) {
                ((HttpPatch) request).setEntity(new StringEntity(data, "UTF-8"));
            }
        }
        HttpClient client = acceptsUntrustedCerts();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONST_TIME_OUT)
                .setConnectTimeout(CONST_TIME_OUT).setConnectionRequestTimeout(CONST_TIME_OUT)
                .build();
        request.setConfig(requestConfig);
        s_logger.info("Making " + request.getMethod() + " request to: " + uri);
        s_logger.info("Query body: " + data);
        HttpResponse httpResponse;
        try {
            httpResponse = client.execute(request);
            int status = httpResponse.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    String json = EntityUtils.toString(entity, "UTF-8");
                    return handleResponse(200, json);
                } else {
                    return handleResponse(200, null);
                }
            } else {
                String eMessage = httpResponse.getStatusLine().getReasonPhrase();
                return handleErrorResponse(status, eMessage);
            }
        } catch (ConnectTimeoutException e) {
            s_logger.error("ConnectTimeoutException: ", e);
        } catch (ParseException e) {
            s_logger.error("ParseException: ", e);
        } catch (IOException e) {
            s_logger.error("IOException: ", e);
        }
        return null;
    }

    public HttpResponse getResponse() {
        return this.response;
    }

    public int getStatusCode() {
        if (response != null) {
            return response.getStatusLine().getStatusCode();
        } else {
            throw new RuntimeException("Request not executed");
        }
    }

    public String getStatusMessage() {
        if (response != null) {
            return response.getStatusLine().getReasonPhrase();
        } else {
            throw new RuntimeException("Request not executed");
        }
    }

    public String getBody() throws IllegalStateException, IOException {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            return readStream(entity.getContent());
        }
        return null;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuffer buff = new StringBuffer();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                buff.append(line);
            }
        } catch (IOException e) {
            s_logger.error("exception: ", e);
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            s_logger.error("exception: ", e);
            e.printStackTrace();
        }
        return buff.toString();
    }

}
