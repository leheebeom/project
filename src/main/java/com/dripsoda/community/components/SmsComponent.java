package com.dripsoda.community.components;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


@Component
public class SmsComponent {

    private static final String ACCESS_KEY = "mWNXKo6Z4DsxV6wruXwi";
    private static final String CALLER_NUMBER = "01066168953";
    private static final String SECRET_KEY = "5MAPglPNrE3t7O8BojUu7XrhNzbujcQuF8RkiWyI";
    private static final String SERVICE_ID = "ncp:sms:kr:292568687843:study";

    public int send(String to, String content) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        long timestamp = System.currentTimeMillis();
        String signature = String.format("POST /sms/v2/services/%s/messages\n%d\n%s",
                SmsComponent.SERVICE_ID,
                timestamp,
                SmsComponent.ACCESS_KEY);
        SecretKeySpec secretKeySpec = new SecretKeySpec(SmsComponent.SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(signature.getBytes(StandardCharsets.UTF_8));
        signature = Base64.encodeBase64String(rawHmac);

        JSONObject bodyJson = new JSONObject();
        bodyJson.put("type", "SMS");
        bodyJson.put("contentType", "COMM");
        bodyJson.put("countryCode", "82");
        bodyJson.put("from", SmsComponent.CALLER_NUMBER);
        bodyJson.put("content", content);
        JSONArray messagesJson = new JSONArray();
        JSONObject messageJson = new JSONObject();
        messageJson.put("to", to);
        messagesJson.put(messageJson);
        bodyJson.put("messages", messagesJson);

        HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://sens.apigw.ntruss.com/sms/v2/services/%s/messages", SmsComponent.SERVICE_ID)).openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("x-ncp-apigw-timestamp", Long.toString(timestamp));
        connection.setRequestProperty("x-ncp-iam-access-key", SmsComponent.ACCESS_KEY);
        connection.setRequestProperty("x-ncp-apigw-signature-v2", signature);
        connection.setRequestMethod("POST");

        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.write(bodyJson.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }

        return connection.getResponseCode();

    }
}
