package chordvers.lanstard.event;

import chordvers.lanstard.KookVoiceChat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class quit implements Listener {
    @EventHandler
    public void quit(PlayerQuitEvent e){
        KookVoiceChat.playerListen.ListenListRemove(e.getPlayer().getName());
    }
}
