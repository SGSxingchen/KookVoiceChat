package chordvers.lanstard.https;

import chordvers.lanstard.KookVoiceChat;

import java.net.*;
import java.io.*;
import java.util.*;

public class GetRequest {
    public static String sendGetRequest(String url, Map<String, Object> params) throws Exception {
        // 如果 params 不为空，将参数拼接到链接地址中
        if (params != null && !params.isEmpty()) {
            StringBuilder sb = new StringBuilder(url);
            sb.append("?");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            url = sb.toString();
        }

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // 设置请求方法为GET
        con.setRequestMethod("GET");

        // 增加 Authorization 头信息
        con.setRequestProperty("Authorization", KookVoiceChat.BotToken);

        int responseCode = con.getResponseCode();
        System.out.println("发送GET请求： " + url);
//        System.out.println("响应代码: " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
