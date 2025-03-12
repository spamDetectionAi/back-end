package com.tsix_hack.spam_ai_detection.service;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WhatsApp {
    public void sendMsg(String to , String message) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("token", System.getenv("token_whatsApp"))
                .add("to", to)
                .add("body", message)
                .build();
        Request request = new Request.Builder()
                .url(System.getenv("URL_API"))
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
}
