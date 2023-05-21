//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package chordvers.lanstard.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class IBuilder {
    public IBuilder() {
    }

    public static ItemStack getBorder(Material material) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(IString.addColor("&7"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack buildItem(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(IString.addColor(name));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack buildItem(Material material, int num) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack.setAmount(num);
        itemMeta.setDisplayName(IString.addColor("&7"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack buildItem(Material material, String name, List<String> lores) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(IString.addColor(name));
        itemMeta.setLore(lores);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
