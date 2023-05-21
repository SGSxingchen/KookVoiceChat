package chordvers.lanstard.command;

import chordvers.lanstard.KookVoiceChat;
import chordvers.lanstard.corn.Channel;
import chordvers.lanstard.corn.OperatePlayerGroups;
import chordvers.lanstard.tools.IString;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class chordvers implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args[0].equalsIgnoreCase("get")){
            if(sender instanceof Player){
                Player player = (Player) sender; // 获取玩家对象
                UUID uuid = player.getUniqueId();
                String uuidString = ((UUID) uuid).toString();
                TextComponent message = new TextComponent(IString.addColor("&b你的UUID是：[" + uuidString+"] 点击即可复制"));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("点击后即可复制").create()));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, uuidString));
                player.spigot().sendMessage(message);
                return true;
            }
            else {
                sender.sendMessage("该命令仅限玩家使用");
                return true;
            }
        }
        if(args[0].equalsIgnoreCase("check")){
            KookVoiceChat.gamePlayerChatMap.print();
            KookVoiceChat.playerListen.print();
        }
        if(args[0].equalsIgnoreCase("run")){
            for (Player player1 : Bukkit.getOnlinePlayers()){
                // 判断是否在监听列表中
                if(KookVoiceChat.playerListen.ListenListContain(player1.getName())){
                    boolean hasPlayer = false;
                    // 遍历在线玩家列表，查找是否有其他玩家在监听列表中，且与当前玩家距离小于等于16格
                    for (Player player2 : Bukkit.getOnlinePlayers()){
                        if(player1 == player2) continue;
                        if(KookVoiceChat.playerListen.ListenListContain(player2.getName())){
                            if(player1.getLocation().distance(player2.getLocation()) <= 16){
                                hasPlayer = true;
                                // 两个玩家距离小于16格
                                // 判断是否有任意一个玩家已经位于群组中
                                if(KookVoiceChat.gamePlayerChatMap.containsPlayer(player1)){
                                    // 玩家1在群组中，所以将玩家2加入玩家1的群组中去
                                    String group_id = KookVoiceChat.gamePlayerChatMap.getGroupIdByPlayer(player1);
                                    String user_id = KookVoiceChat.playerListen.getPlayerId(player2.getName());
                                    Channel.moveVoiceChannel(group_id,user_id);

                                    KookVoiceChat.gamePlayerChatMap.addPlayer(player2,group_id);
                                }
                                else if(KookVoiceChat.gamePlayerChatMap.containsPlayer(player2)){
                                    String group_id = KookVoiceChat.gamePlayerChatMap.getGroupIdByPlayer(player2);
                                    String user_id = KookVoiceChat.playerListen.getPlayerId(player1.getName());
                                    Channel.moveVoiceChannel(group_id,user_id);
                                    KookVoiceChat.gamePlayerChatMap.addPlayer(player1,group_id);
                                }
                                else {
                                    // 两个人都不在的情况下，新建一个频道
                                    String NewGroupId = Channel.createVoiceChanel(KookVoiceChat.ServerId,"临时语音频道");
                                    String user1_id = KookVoiceChat.playerListen.getPlayerId(player1.getName());
                                    String user2_id = KookVoiceChat.playerListen.getPlayerId(player2.getName());
                                    Channel.moveVoiceChannel(NewGroupId,user1_id);
                                    Channel.moveVoiceChannel(NewGroupId,user2_id);
                                    KookVoiceChat.gamePlayerChatMap.addPlayer(player1,NewGroupId);
                                    KookVoiceChat.gamePlayerChatMap.addPlayer(player2,NewGroupId);
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
        return true;
    }
}
