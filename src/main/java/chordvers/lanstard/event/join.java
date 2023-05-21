package chordvers.lanstard.event;

import chordvers.lanstard.KookVoiceChat;
import chordvers.lanstard.https.GetRequest;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;

public class join implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent event){
        // 准备请求参数
        Map<String,Object> params = new HashMap<>();
        params.put("channel_id", KookVoiceChat.StartChannelId);

        // 发送GET请求获取当前房间的用户列表
        String response = null;
        try {
            response = GetRequest.sendGetRequest("https://www.kookapp.cn/api/v3/channel/user-list",params);

            // 解析JSON数据
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(response);
            JSONArray data = (JSONArray) jsonObject.get("data");
            String playerName = event.getPlayer().getName();
            for(String s : KookVoiceChat.BindList){
                String[] ss = s.split(",");
                if(ss[1].equalsIgnoreCase(playerName)){
                    for (Object datum : data) {
                        JSONObject obj = (JSONObject) datum;
                        String id = obj.get("id").toString();
                        if (ss[0].equalsIgnoreCase(id)) {
                            KookVoiceChat.playerListen.ListenListAdd(ss[1], ss[0]); // 将对应的语音频道ID加入到监听列表中
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            // 发生异常则抛出运行时异常
            throw new RuntimeException(e);
        }
    }
}
