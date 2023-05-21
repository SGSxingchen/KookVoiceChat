package chordvers.lanstard;

import chordvers.lanstard.command.chordvers;
import chordvers.lanstard.corn.Channel;
import chordvers.lanstard.corn.OperatePlayerGroups;
import chordvers.lanstard.corn.deleteEmptyChannel;
import chordvers.lanstard.event.join;
import chordvers.lanstard.event.quit;
import chordvers.lanstard.https.GetRequest;
import chordvers.lanstard.list.GamePlayerChatMap;
import chordvers.lanstard.list.PlayerListen;
import chordvers.lanstard.tools.ConfigManager;
import chordvers.lanstard.websocket.SocketClient;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


public final class KookVoiceChat extends JavaPlugin {
    public static SocketClient client;
    public static int sn = 0;
    public static Plugin plugin;

    public static List<String> BindList = new ArrayList<>();
    public static String StartChannelId = "0";
    public static String ServerId = "0";
    public static String BotToken = null;

    public static PlayerListen playerListen = new PlayerListen();
    public static GamePlayerChatMap gamePlayerChatMap = new GamePlayerChatMap();

    @Override
    public void onEnable() {
        plugin = this;
        WebSocketInit();
        FileInit();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this,new OperatePlayerGroups(),0,20);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this,new deleteEmptyChannel(),0,10);
        Bukkit.getPluginCommand("chordvers").setExecutor(new chordvers());
        Bukkit.getPluginManager().registerEvents(new join(), this);
        Bukkit.getPluginManager().registerEvents(new quit(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ConfigManager.writeConfig("config","BindList",BindList);
        for(String s : gamePlayerChatMap.getGroupList()){
            Channel.deleteVoiceChanel(s);
        }
    }
    void FileInit(){
        plugin.saveDefaultConfig();
        BindList = ConfigManager.getConfig("config").getStringList("BindList");
        StartChannelId = ConfigManager.getConfig("config").getString("StartChannelId");
        ServerId = ConfigManager.getConfig("config").getString("ServerId");
        BotToken = ConfigManager.getConfig("config").getString("BotToken");
        if(StartChannelId.equalsIgnoreCase("0")){
            System.out.println("初始语音频道还未绑定，请在kook中输入指令/channel获取");
        }
        else {
            System.out.println(String.format("初始语音频道已绑定:%s",StartChannelId));
        }
        if(ServerId.equalsIgnoreCase("0")){
            System.out.println("初始语音服务器还未设置，请在kook中输入指令/server获取");
        }
        else {
            System.out.println(String.format("初始语音服务器已绑定:%s",ServerId));
        }
        if(BotToken.equalsIgnoreCase("0")){
            System.out.println("机器人token还未填写！");
        }
        else {
            System.out.println(String.format("机器人TOKEN已写入",BotToken));
        }
//        if(BindList.isEmpty()){
//            Map<?,?> map = new HashMap<>();
//            BindList.add(map);
//            ConfigManager.writeConfig("config","BindList",BindList);
//        }

    }
    private void WebSocketInit(){
        String response = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("compress",0);
            response = GetRequest.sendGetRequest("https://www.kookapp.cn/api/v3/gateway/index", params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String url = null;
        JSONParser parser = new JSONParser();
        try {
            // 将 JSON 文本解析为 JSONObject 对象
            JSONObject jsonObject = (JSONObject) parser.parse(response);
            // 获取 data 中的 JSONObject
            JSONObject dataObject = (JSONObject) jsonObject.get("data");
            // 获取 url 值
            url = (String) dataObject.get("url");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            client = new SocketClient(new URI(url));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        client.connect(); // 连接到服务器
        run();
    }
    public void run(){
        System.out.println("开始发送心跳包");
        Bukkit.getScheduler().runTaskTimerAsynchronously(this,()->{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("s", 2);
            jsonObject.put("sn", sn);
            String jsonString = jsonObject.toString();

            KookVoiceChat.client.send(jsonString);
            System.out.println("心跳包消息："+jsonString);
        },0,30*20);
    }
}
