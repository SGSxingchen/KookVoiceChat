//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package chordvers.lanstard.tools;

import chordvers.lanstard.KookVoiceChat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    public ConfigManager() {
    }

    public static void createFile(String s) {
        File file = new File(KookVoiceChat.plugin.getDataFolder(), s + ".yml");
        if (!file.exists()) {
            KookVoiceChat.plugin.saveResource(s + ".yml", false);
        }

    }

    public static FileConfiguration getConfig(String s) {
        File file = new File(KookVoiceChat.plugin.getDataFolder(), s + ".yml");
        if (!file.exists()) {
            KookVoiceChat.plugin.saveResource(s, false);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public static void writeConfig(String s, String node, Object value) {
        FileConfiguration fileConfiguration = getConfig(s);
        fileConfiguration.set(node, value);

        try {
            fileConfiguration.save(new File(KookVoiceChat.plugin.getDataFolder(), s + ".yml"));
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }
}
