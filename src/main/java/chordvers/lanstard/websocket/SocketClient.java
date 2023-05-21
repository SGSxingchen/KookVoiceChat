package chordvers.lanstard.websocket;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import chordvers.lanstard.KookVoiceChat;
import chordvers.lanstard.https.GetRequest;
import chordvers.lanstard.https.PostRequest;
import chordvers.lanstard.tools.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SocketClient extends WebSocketClient {
    public SocketClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("连接成功: "+serverHandshake.getHttpStatusMessage());
    }
//0	server->client	消息(包含聊天和通知消息)
//1	server->client	客户端连接 ws 时, 服务端返回握手结果
//2	client->server	心跳，ping
//3	server->client	心跳，pong
//4	client->server	resume, 恢复会话
//5	server->client	reconnect, 要求客户端断开当前连接重新连接
//6	server->client	resume ack
    @Override
    public void onMessage(String message) {
//        System.out.println("服务侧消息："+message);
        updata(message);
        bind_check(message);
        check_check(message);
        channel_check(message);
        joined_channel_check(message);
        exited_channel_check(message);
        server_check(message);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
    }

    @Override
    public void onError(Exception e) {

    }
    void updata(String message){
        //System.out.println("服务侧消息："+message);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(message);
            if(jsonObject.get("sn")!=null) {
                String s = jsonObject.get("sn").toString();
                KookVoiceChat.sn = Integer.parseInt(s);
//                System.out.println(KookVoiceChat.sn);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    void bind_check(String message){
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(message);
            if(jsonObject.get("d")!=null) {
                JSONObject dataObject = (JSONObject) jsonObject.get("d");
                if (dataObject.get("content") != null) {
                    String content = dataObject.get("content").toString();
                    String[] contents = content.split(" ");
                    String author_id = dataObject.get("author_id").toString();
                    if (contents[0].equalsIgnoreCase("/bind")) {
                        System.out.println("/bind检查通过");
                        String data = contents[1] + "," + contents[2];
                        String bind = author_id + "," + data;
                        if (!KookVoiceChat.BindList.contains(bind)) {
                            System.out.println("添加对应数据");
                            KookVoiceChat.BindList.add(bind);
                            ConfigManager.writeConfig("config", "BindList", KookVoiceChat.BindList);
                        }
                        if (dataObject.get("channel_type").toString().equalsIgnoreCase("GROUP")) {
                            sendPublicMessage("绑定成功！",message,9);
                        }
                        else if (dataObject.get("channel_type").toString().equalsIgnoreCase("PERSON")) {
                            sendPersonalMessage("绑定成功！",message,9);
                        }

                    }
                }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    void check_check(String message){
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(message);
            if (jsonObject.get("d") != null) {
                JSONObject dataObject = (JSONObject) jsonObject.get("d");
                if (dataObject.get("content") != null) {
                    if(dataObject.get("content").toString().equalsIgnoreCase("/check")) {
                        System.out.println("/check检查通过");
                        String author_id = dataObject.get("author_id").toString();
                        JSONObject dataObject2 = (JSONObject) dataObject.get("extra");
                        JSONObject dataObject3 = (JSONObject) dataObject2.get("author");
                        String username = dataObject3.get("username").toString();
                        String avatar_url = dataObject3.get("avatar").toString();
                        String mcid = "未绑定";
                        String uuid = "未绑定";
                        for(String s : KookVoiceChat.BindList){
                            String[] ss =s.split(",");
                            if(ss[0].equalsIgnoreCase(author_id)){
                                mcid = ss[1];
                                uuid = ss[2];
                                break;
                            }
                        }
                        String content = String.format("[{\"type\":\"card\",\"theme\":\"secondary\",\"size\":\"lg\",\"modules\":[{\"type\":\"section\",\"text\":{\"type\":\"plain-text\",\"content\":\"%s,你的玩家信息如下：\"}},{\"type\":\"divider\"},{\"type\":\"section\",\"text\":{\"type\":\"paragraph\",\"cols\":3,\"fields\":[{\"type\":\"kmarkdown\",\"content\":\"**昵称**\\n%s\"},{\"type\":\"kmarkdown\",\"content\":\"**用户ID**\\n%s\"}],\"mode\":\"left\",\"accessory\":{\"type\":\"image\",\"src\":\"%s\",\"size\":\"lg\"}}},{\"type\":\"divider\"},{\"type\":\"section\",\"text\":{\"type\":\"plain-text\",\"content\":\"MCID：%s\"}},{\"type\":\"divider\"},{\"type\":\"section\",\"text\":{\"type\":\"plain-text\",\"content\":\"MCUUID：%s\"}}]}]", username, username, author_id, avatar_url, mcid, uuid);
                        System.out.println(content);
                        if (dataObject.get("channel_type").toString().equalsIgnoreCase("GROUP")) {
//                            String target_id = dataObject.get("target_id").toString();
//                            String msg_id = dataObject.get("msg_id").toString();
//                            sendPublicMessage(content,msg_id,target_id);
                            sendPublicMessage(content,message,10);
//                            JSONObject json = new JSONObject();
//                            json.put("target_id", target_id);
//                            json.put("type", 10);
//                            json.put("content", content);
//                            json.put("quote", msg_id);
//                            String jsonString = json.toJSONString();
//                            try {
//                                String response = PostRequest.postRequest("https://www.kookapp.cn/api/v3/message/create", jsonString);
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }

                        } else if (dataObject.get("channel_type").toString().equalsIgnoreCase("PERSON")) {
                            //sendPersonalMessage(content,dataObject.get("msg_id").toString(),author_id);
                            sendPersonalMessage(content,message,10);
//                            System.out.println("发送私人消息");
//                            String msg_id = dataObject.get("msg_id").toString();
//                            JSONObject json = new JSONObject();
//                            json.put("target_id", author_id);
//                            json.put("type", 10);
//                            json.put("content", content);
//                            json.put("quote", msg_id);
//                            String jsonString = json.toJSONString();
//                            try {
//                                String response = PostRequest.postRequest("https://www.kookapp.cn/api/v3/direct-message/create", jsonString);
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }
                        }
                    }
                }
            }
        }catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    void channel_check(String message){
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(message);
            if (jsonObject.get("d") != null) {
                JSONObject d = (JSONObject) jsonObject.get("d");

                if (d.get("content") != null) {
                    String channel_type = d.get("channel_type").toString();
                    if (d.get("content").toString().equalsIgnoreCase("/server") && channel_type.equalsIgnoreCase("GROUP")) {
                        System.out.println("/server检查通过");
                        if (d.get("extra") != null) {
                            JSONObject extra = (JSONObject) d.get("extra");
                            String guild_id = extra.get("guild_id").toString();
                            //获取发送用户所在的语音频道id
                            sendPublicMessage(String.format("你目前所处语音频道ID为：%s，请写入服务器配置文件后重启服务器",guild_id),message,9);

                        }
                    }
                    else if(d.get("content").toString().equalsIgnoreCase("/server")){
                        sendPersonalMessage("此命令无法在私聊中使用",message,9);
                    }
                }
            }
        }
        catch (ParseException e){
            throw new RuntimeException(e);
        }
    }
    void server_check(String message){
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(message);
            if (jsonObject.get("d") != null) {
                JSONObject d = (JSONObject) jsonObject.get("d");

                if (d.get("content") != null) {
                    String channel_type = d.get("channel_type").toString();
                    String user_id = d.get("author_id").toString();
                    if (d.get("content").toString().equalsIgnoreCase("/channel") && channel_type.equalsIgnoreCase("GROUP")) {
                        System.out.println("/channel检查通过");
                        if (d.get("extra") != null) {
                            JSONObject extra = (JSONObject) d.get("extra");
                            String guild_id = extra.get("guild_id").toString();
                            //获取发送用户所在的语音频道id
                            Map<String, Object> params = new HashMap<>();
                            params.put("guild_id", guild_id);
                            params.put("user_id", user_id);
                            String response = null;
                            try {
                                response = GetRequest.sendGetRequest("https://www.kookapp.cn/api/v3/channel-user/get-joined-channel", params);
                                JSONParser parser2 = new JSONParser();
                                JSONObject data1 = (JSONObject) parser2.parse(response);
                                JSONObject data = (JSONObject) data1.get("data");
                                JSONArray items = (JSONArray) data.get("items");
                                if (items.isEmpty()) {
                                    // "items" 数组为空
                                    sendPublicMessage("你还没有加入语音频道，请加入后重试",message,9);
                                } else {
                                    // "items" 数组不为空
                                    JSONObject item = (JSONObject) items.get(0);
                                    String channel_id = item.get("id").toString();
                                    sendPublicMessage(String.format("你目前所处语音频道ID为：%s，请写入服务器配置文件后重启服务器",channel_id),message,9);
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    else if(d.get("content").toString().equalsIgnoreCase("/channel")){
                        sendPersonalMessage("此命令无法在私聊中使用",message,9);
                    }
                }
            }
        }
        catch (ParseException e){
            throw new RuntimeException(e);
        }
    }
    void joined_channel_check(String message){
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(message);
            if(jsonObject.get("d")!=null){
                JSONObject data = (JSONObject) jsonObject.get("d");
                if(data.get("extra")!=null){
                    JSONObject data2 = (JSONObject) data.get("extra");
                    if(data2.get("type")!=null){
                        String type = data2.get("type").toString();
                        if(data2.get("body")!=null){
                            JSONObject body = (JSONObject) data2.get("body");
                            if(body.get("user_id") != null) {
                                String user_id = body.get("user_id").toString();
                                if(body.get("channel_id") != null){
                                   String channel_id = body.get("channel_id").toString();
                                    if(type.equalsIgnoreCase("joined_channel")&&channel_id.equalsIgnoreCase(KookVoiceChat.StartChannelId)){
                                        //条件1：已经绑定
                                        //条件2：处于游戏中
                                        //判定
                                        for(String s : KookVoiceChat.BindList) {
                                            String[] ss = s.split(",");
                                            if (ss[0].equalsIgnoreCase(user_id)) {
                                                Player p = Bukkit.getPlayer(ss[1]);
                                                if(Bukkit.getOnlinePlayers().contains(p)){
                                                    KookVoiceChat.playerListen.ListenListAdd(ss[1],ss[0]);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
    void exited_channel_check(String message){
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(message);
            if(jsonObject.get("d")!=null){
                JSONObject data = (JSONObject) jsonObject.get("d");
                if(data.get("extra")!=null){
                    JSONObject data2 = (JSONObject) data.get("extra");
                    if(data2.get("type")!=null){
                        String type = data2.get("type").toString();
                        if(data2.get("body")!=null) {
                            JSONObject body = (JSONObject) data2.get("body");
                            if(body.get("user_id")!=null) {
                                String user_id = body.get("user_id").toString();
                                if(body.get("channel_id") != null) {
                                    String channel_id = body.get("channel_id").toString();
                                    if (type.equalsIgnoreCase("exited_channel")) {
                                        for (String s : KookVoiceChat.BindList) {
                                            String[] ss = s.split(",");
                                            if (ss[0].equalsIgnoreCase(user_id) && !channel_id.equalsIgnoreCase(KookVoiceChat.StartChannelId) &&!KookVoiceChat.gamePlayerChatMap.containsGuild(channel_id)) {
                                                KookVoiceChat.playerListen.ListenListRemove(ss[1]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    void sendPersonalMessage(String content,String data,int type){
        JSONParser parser = new JSONParser();
        String author_id = null;
        String msg_id = null;
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(data);
            JSONObject d = (JSONObject) jsonObject.get("d");
            author_id = d.get("author_id").toString();
            msg_id = d.get("msg_id").toString();
        }catch (ParseException e) {
            throw new RuntimeException(e);
        }
        JSONObject json = new JSONObject();
        json.put("target_id", author_id);
        json.put("type", type);
        json.put("content", content);
        json.put("quote", msg_id);
        String jsonString = json.toJSONString();
        //System.out.println(author_id+content+msg_id);
        try {
            String response = PostRequest.postRequest("https://www.kookapp.cn/api/v3/direct-message/create", jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    void sendPersonalMessage(String content,String msg_id,String author_id){
       // System.out.println("发送群组消息");
        JSONObject json = new JSONObject();
        json.put("target_id", author_id);
        json.put("type", 10);
        json.put("content", content);
        json.put("quote", msg_id);
        String jsonString = json.toJSONString();
        try {
            String response = PostRequest.postRequest("https://www.kookapp.cn/api/v3/direct-message/create", jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    void sendPublicMessage(String content,String msg_id,String target_id){
       // System.out.println("发送群组消息");
        JSONObject json = new JSONObject();
        json.put("target_id", target_id);
        json.put("type", 10);
        json.put("content", content);
        json.put("quote", msg_id);
        String jsonString = json.toJSONString();
        try {
            String response = PostRequest.postRequest("https://www.kookapp.cn/api/v3/message/create", jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    void sendPublicMessage(String content,String data,int type){
        JSONParser parser = new JSONParser();
        String target_id = null;
        String msg_id = null;
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(data);
            JSONObject d = (JSONObject) jsonObject.get("d");
            target_id = d.get("target_id").toString();
            msg_id = d.get("msg_id").toString();
        }catch (ParseException e) {
            throw new RuntimeException(e);
        }
       // System.out.println("发送群组消息");
        JSONObject json = new JSONObject();
        json.put("target_id", target_id);
        json.put("type", type);
        json.put("content", content);
        json.put("quote", msg_id);
        String jsonString = json.toJSONString();
        try {
            String response = PostRequest.postRequest("https://www.kookapp.cn/api/v3/message/create", jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}