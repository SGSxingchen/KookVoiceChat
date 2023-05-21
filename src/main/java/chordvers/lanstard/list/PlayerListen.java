package chordvers.lanstard.list;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerListen {

    private Set<String> PlayerListenList;
    private Map<String,String> PlayerToUserId;
    public PlayerListen(){
        PlayerListenList = new HashSet<>();
        PlayerToUserId = new HashMap<>();
    }
    public void print(){
        System.out.println("玩家监听列表："+PlayerListenList);
        System.out.println("玩家对应KOOKID列表："+PlayerToUserId);
    }
    public Set<String> GetList(){
        return this.PlayerListenList;
    }
    public void ListenListAdd(String player,String id){
        PlayerListenList.add(player);
        PlayerToUserId.put(player,id);
    }
    public void ListenListRemove(String player){
        PlayerListenList.remove(player);
        PlayerToUserId.remove(player);
    }
    public boolean ListenListContain(String name){
        return PlayerListenList.contains(name);
    }
    public String getPlayerId(String player){
        return PlayerToUserId.get(player);
    }
}
