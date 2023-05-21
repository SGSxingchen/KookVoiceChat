package chordvers.lanstard.https;

import chordvers.lanstard.KookVoiceChat;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.net.*;
import java.io.*;
import java.util.*;

public class PostRequest {
    public static String postRequest(String url, String json) throws UnirestException {
        System.out.println("发送了一次POST请求"+url);
        HttpResponse<String> response = (HttpResponse<String>) Unirest.post(url)
                .header("Authorization", KookVoiceChat.BotToken)
                .header("Content-Type", "application/json")
                .body(json)
                .asString();
        int statusCode = response.getStatus();
//        System.out.println("POST请求响应码"+statusCode);
//        System.out.println(response.getBody());
        return response.getBody();
    }
}