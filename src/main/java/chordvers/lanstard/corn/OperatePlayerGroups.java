package chordvers.lanstard.corn;

import chordvers.lanstard.KookVoiceChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.swing.plaf.basic.BasicButtonUI;


// 定义OperatePlayerGroups类实现Runnable接口
public class OperatePlayerGroups implements Runnable {
    // 重写run方法
    @Override
    public void run() {

        // 遍历在线玩家列表
        for (Player player1 : Bukkit.getOnlinePlayers()){
            // 判断是否在监听列表中
            if(KookVoiceChat.playerListen.ListenListContain(player1.getName())){
                boolean hasPlayer = false;
                // 遍历在线玩家列表，查找是否有其他玩家在监听列表中，且与当前玩家距离小于等于16格
                for (Player player2 : Bukkit.getOnlinePlayers()){
                    if(player1 == player2){
                        continue;
                    }
                    if(KookVoiceChat.playerListen.ListenListContain(player2.getName())){
                        if(player1.getLocation().distance(player2.getLocation()) <= 16){
                            hasPlayer = true;
                            // 两个玩家距离小于16格
                            // 判断是否有任意一个玩家已经位于群组中
                            if(KookVoiceChat.gamePlayerChatMap.containsPlayer(player1)&&!KookVoiceChat.gamePlayerChatMap.containsPlayer(player2)){
                                System.out.println(String.format("玩家1处于群组而玩家2不在"));
                                // 玩家1在群组中，所以将玩家2加入玩家1的群组中去
                                String group_id = KookVoiceChat.gamePlayerChatMap.getGroupIdByPlayer(player1);
                                String user_id = KookVoiceChat.playerListen.getPlayerId(player2.getName());
                                KookVoiceChat.gamePlayerChatMap.addPlayer(player2,group_id);
                                Channel.moveVoiceChannel(group_id,user_id);


                            }
                            else if(KookVoiceChat.gamePlayerChatMap.containsPlayer(player2)&&!KookVoiceChat.gamePlayerChatMap.containsPlayer(player1)){
                                System.out.println(String.format("玩家2处于群组而玩家1不在"));
                                String group_id = KookVoiceChat.gamePlayerChatMap.getGroupIdByPlayer(player2);
                                String user_id = KookVoiceChat.playerListen.getPlayerId(player1.getName());
                                KookVoiceChat.gamePlayerChatMap.addPlayer(player1,group_id);
                                Channel.moveVoiceChannel(group_id,user_id);

                            }
                            else if(!KookVoiceChat.gamePlayerChatMap.containsPlayer(player2)&&!KookVoiceChat.gamePlayerChatMap.containsPlayer(player1)){
                                // 两个人都不在的情况下，新建一个频道
                                System.out.println(String.format("两人都不在，新建一个频道"));
                                String NewGroupId = Channel.createVoiceChanel(KookVoiceChat.ServerId,"临时语音频道");
                                String user1_id = KookVoiceChat.playerListen.getPlayerId(player1.getName());
                                String user2_id = KookVoiceChat.playerListen.getPlayerId(player2.getName());
                                KookVoiceChat.gamePlayerChatMap.addPlayer(player1,NewGroupId);
                                KookVoiceChat.gamePlayerChatMap.addPlayer(player2,NewGroupId);
                                Channel.moveVoiceChannel(NewGroupId,user1_id);
                                Channel.moveVoiceChannel(NewGroupId,user2_id);

                            }
                        }
                    }
                }
                // 如果没有其他在监听列表中的玩家，且当前玩家在群组中，将其移出群组
                if(!hasPlayer && KookVoiceChat.gamePlayerChatMap.containsPlayer(player1)){
                    System.out.println(String.format("玩家%s附近没有其他玩家",player1.getName()));
                    String user_id = KookVoiceChat.playerListen.getPlayerId(player1.getName());
                    String group_id = KookVoiceChat.gamePlayerChatMap.getGroupIdByPlayer(player1);
                    Channel.moveVoiceChannel(KookVoiceChat.StartChannelId,user_id);
                    KookVoiceChat.gamePlayerChatMap.removePlayer(player1,group_id);
                }
            }
        }
    }
}
