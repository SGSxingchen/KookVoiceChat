package chordvers.lanstard.corn;

import chordvers.lanstard.KookVoiceChat;
import chordvers.lanstard.https.PostRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

public class Channel {
    public static String createVoiceChanel(String guild_id,String name){
        System.out.println(String.format("创建频道%s",guild_id));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("guild_id",guild_id);
        jsonObject.put("name",name);
        jsonObject.put("type",2);
        jsonObject.put("limit_amount",99);
        jsonObject.put("voice_quality",1);
        String jsonString = jsonObject.toJSONString();
        String reponse = null;
        try {
            reponse = PostRequest.postRequest("https://www.kookapp.cn/api/v3/channel/create",jsonString);
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(reponse);
            JSONObject data = (JSONObject) object.get("data");
            KookVoiceChat.gamePlayerChatMap.addNewChannel(data.get("id").toString());
            return data.get("id").toString();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public static void deleteVoiceChanel(String channel_id){
        KookVoiceChat.gamePlayerChatMap.deleteNewChannel(channel_id);
        System.out.println(String.format("删除频道%s",channel_id));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("channel_id",channel_id);
        String jsonString = jsonObject.toJSONString();
        String reponse = null;
        try {
            reponse = PostRequest.postRequest("https://www.kookapp.cn/api/v3/channel/delete",jsonString);

        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public static void moveVoiceChannel(String target_id,String user_id){
        System.out.println(String.format("%s移动到频道%s",user_id,target_id));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("target_id",target_id);
        List<String> ids = new ArrayList<>();
        ids.add(user_id);
        jsonObject.put("user_ids",ids);
        String jsonString = jsonObject.toJSONString();
        String reponse = null;
        try {
            reponse = PostRequest.postRequest("https://www.kookapp.cn/api/v3/channel/move-user",jsonString);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}

