package chordvers.lanstard.list;

import org.bukkit.entity.Player;

import java.util.*;

public class GamePlayerChatMap {
    private HashMap<String, Set<Player>> GroupIdToPlayerList ;
    private HashMap<Player,String> PlayerToGroupId;
    private Set<String> GuildList;
    public void print(){
        System.out.println("每频道对应玩家列表："+GroupIdToPlayerList);
        System.out.println("玩家对应自身语音频道列表："+PlayerToGroupId);
        System.out.println("频道列表："+GuildList);
    }
    public GamePlayerChatMap(){
        GroupIdToPlayerList = new HashMap<>();
        PlayerToGroupId = new HashMap<>();
        GuildList = new HashSet<>();
    }
    public void addNewChannel(String id){
        GuildList.add(id);
    }
    public void deleteNewChannel(String id){
        GuildList.remove(id);
        GroupIdToPlayerList.remove(id);
    }
    public boolean containsPlayer(Player p ){
        return PlayerToGroupId.containsKey(p);
    }
    public String getGuildId(Player p){
        return PlayerToGroupId.get(p);
    }
    public void addPlayer(Player p , String id){
        PlayerToGroupId.put(p,id);
        if(GroupIdToPlayerList.containsKey(id)){
            Set<Player> t = GroupIdToPlayerList.get(id);
            t.add(p);
            GroupIdToPlayerList.replace(id,t);
        }
        else {
            //新频道
            Set<Player> t = new HashSet<>();
            t.add(p);
            GroupIdToPlayerList.put(id,t);
        }
    }
    public void removePlayer(Player p, String id){
        PlayerToGroupId.remove(p);
        Set<Player> t = GroupIdToPlayerList.get(id);
        t.remove(p);
        GroupIdToPlayerList.replace(id,t);
//        if(t.size() <= 1){
//            //一个群组小于两个人
//            GroupIdToPlayerList.remove(id);
//        }
    }
    public String getGroupIdByPlayer(Player p){
        return PlayerToGroupId.get(p);
    }
    public boolean containsGuild(String id){
        return GuildList.contains(id);
    }
    public Set<String> getGroupList(){
        return this.GuildList;
    }
    public boolean groupIsEmpty(String id){
        Set<Player> playerList = GroupIdToPlayerList.get(id);
        return playerList.isEmpty();
    }
}
